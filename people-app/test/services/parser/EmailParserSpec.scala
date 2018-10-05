package services.parser

import org.scalatest.AsyncFlatSpec
import scala.concurrent.Future


class EmailParserSpec extends AsyncFlatSpec {
  "EmailParser" should "get a list of the characters and their occurrence frequency" in {
    val emails = List("t@aol.com", "p@aol.com")

    val counter = new EmailParser()
    val counts = counter.lookupCharFrequency(Future(emails))
    counts.map { chars =>
      assert(chars.size == 9)
      assert(chars.find(_.char == 'a').get.count == 2)
      assert(chars.find(_.char == 'p').get.count == 1)
    }
  }

  it should "determine possible duplicate emails with a heavy right sided similarity" in {
    val dupedEmailsOne = List("jJones@hotmail.com", "jonesjones@hotmail.com")
    val dupedEmailsTwo = List("parkparker@gmail.com", "pparker@gmail.com")
    val emails = "fakenondupe@gmail.com" :: dupedEmailsOne ++ dupedEmailsTwo
    val emailInput = Future(emails)

    val parser = new EmailParser()
    val dupes = parser.possibleDuplicates(emailInput)
    dupes.map { d =>
      assert(d == List(dupedEmailsOne, dupedEmailsTwo))
    }
  }

  it should "determine possible duplicate emails with a heavy left sided similarity" in {
    val dupedEmails = List("tonys@gmail.com", "tonystark@gmail.com")
    val emails: List[String] = "fakenondupe@gmail.com" :: dupedEmails
    val emailInput = Future(emails)

    val parser = new EmailParser()
    val dupes = parser.possibleDuplicates(emailInput)
    dupes.map { d =>
      assert(d == List(dupedEmails))
    }
  }

  it should "remove duplicates of the found duplicate emails with a heavy left sided similarity" in {
    val dupedEmails = List("tonys@gmail.com", "tonystark@gmail.com")
    val emails: List[String] = "fakenondupe@gmail.com" :: dupedEmails
    val emailInput = Future(emails)

    val parser = new EmailParser()
    val dupes = parser.possibleDuplicates(emailInput)
    dupes.map { d =>
      assert(d == List(dupedEmails))
    }
  }

  it should "remove domain from emails" in {
    val email = "parker@gmail.com"
    val localPart = EmailParser.onlylocalPart(email)
    assert(localPart == "parker")
  }
}
