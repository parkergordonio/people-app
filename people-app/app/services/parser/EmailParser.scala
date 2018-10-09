package services.parser

import javax.inject.Inject

import models.charCount.CharCountPair
import services.parser.DuplicatePredictor.Words.Rules.Rule
import services.parser.DuplicatePredictor.Words.getSizeRules

import scala.concurrent.{ExecutionContext, Future}

class EmailParser @Inject()(implicit val ex: ExecutionContext) extends ParsingService {
  import EmailParser._

  override def lookupCharFrequency(wordsFut: Future[List[String]]): Future[Seq[CharCountPair]] = {
    wordsFut.map { words =>
      val chars = words.flatMap(_.toList)

      val counts = chars.groupBy(c => c).map {
        case (char, list) => CharCountPair(char, list.size)
      }
      counts.toSeq.sortWith(_.count > _.count)
    }
  }


  override def possibleDuplicates(emailFut: Future[List[String]]): Future[Seq[List[String]]] = {
    def fullAddressOnly(e: Seq[List[Email]]): Seq[List[String]] = e.map(_.map(_.fullAddress))

    emailFut.map { emails =>
      val localParts = emails.map(toEmail)
      fullAddressOnly(findDuplicates(localParts))
    }
  }
}

object EmailParser {

  case class Email(fullAddress: String, local: String)

  def removeSameListing(items: List[List[Email]]): List[List[Email]] = {
    val sorted = items.map(_.sortBy(_.local))
    sorted.distinct
  }

  def toEmail(fullAddress: String): Email = {
    val lowercaseLocal = onlylocalPart(fullAddress).toLowerCase()
    Email(fullAddress, lowercaseLocal)
  }

  def onlylocalPart(s: String): String = s.split('@')(0)

  private def getDupes(email1: Email, email2: Email): Option[List[Email]] = {
    val local = (email1.local, email2.local)
    val rules = DuplicatePredictor.getRules(local)
    val foundDuplicate = rules.exists(_.isDuplicate(local) == true)
    println(s"Found duplicate ${foundDuplicate}")
    if (foundDuplicate) Some(List(email1, email2)) else None
  }

  def findDuplicates(emails: List[Email]): Seq[List[Email]] = {
    val zipped = emails.zipWithIndex

    def theSameEmail(e1: (Email, Int), e2: (Email, Int)): Boolean = e1._2 == e2._2

    val dupes = zipped.flatMap { email =>
      zipped.flatMap { innerEmail =>
        if (!theSameEmail(email, innerEmail)) {
          getDupes(email._1, innerEmail._1)
        } else {
          None
        }
      }
    }
    println(s"Found Dupes: ${dupes}")
    removeSameListing(dupes)
  }
}

object DuplicatePredictor {

  private def sortWordSize(words: (String, String)): (String, String) = {
    if (words._1.length < words._2.length)
      (words._1, words._2)
    else
      (words._2, words._1)
  }

  def getRules(words: (String, String)): List[Rule] = {
    val (smallestWord, _) = sortWordSize(words)
    getSizeRules(smallestWord)
  }

  object Words {
    import Rules._
    val SMALL_MAX_LENGTH = 4

    def getSizeRules(word: String): List[Rule] = {
      word.length match {
        case l if l > SMALL_MAX_LENGTH => {
          // println("Using rules for Large Words")
          LargeWord.rules()
        }
        case _ => {
          // println("Using Rules for Small words")
          SmallWord.rules()
        }
      }
    }

    object Rules {

      trait Rule {
        def isDuplicate(words: (String, String)): Boolean
      }

      case class LeftSamenessRule_Large(threshold: Int) extends Rule {
        private def rightSubRemoved(charsToRemove: Int, word: String): String = {
          val lastCharIndex = word.length - charsToRemove
          word.substring(0, lastCharIndex)
        }

        override def isDuplicate(words: (String, String)): Boolean = {
          val (smallWord, largeWord) = sortWordSize(words)
          val oneCharRemoved = rightSubRemoved(threshold, smallWord)
          val leftSubTrimmed = largeWord.substring(0, oneCharRemoved.length)
          println(s"Comparing dupe: ${leftSubTrimmed}, ${oneCharRemoved}")
          leftSubTrimmed == oneCharRemoved
        }
      }

      case class RightSamenessRule_Large(threshold: Int) extends Rule {
        override def isDuplicate(words: (String, String)): Boolean = {
          val reversedOne = words._1.reverse
          val reversedTwo = words._2.reverse
          println(s"Reversed: ${reversedOne}, ${reversedTwo}")
          LeftSamenessRule_Large(threshold).isDuplicate((reversedOne, reversedTwo))
        }
      }

      trait WordRules {
        def rules(): List[Rule]
      }

      case object SmallWord extends WordRules {
        override def rules(): List[Rule] = {
          List()
        }
      }

      case object LargeWord extends WordRules {
        override def rules(): List[Rule] = {
          val THRESHOLD_FACTOR = 1

          List(
            LeftSamenessRule_Large(THRESHOLD_FACTOR),
            RightSamenessRule_Large(THRESHOLD_FACTOR)
          )
        }
      }
    }
  }
}