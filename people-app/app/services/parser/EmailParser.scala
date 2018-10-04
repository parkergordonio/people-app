package services.parser

import javax.inject.Inject

import models.CharCount.CharCountPair

import scala.concurrent.{ExecutionContext, Future}

class EmailParser @Inject()(implicit val ex: ExecutionContext) extends ParsingService {
  override def lookupCharFrequency(wordsFut: Future[List[String]]): Future[Seq[CharCountPair]] = {
    wordsFut.map { words =>
      val chars = words.flatMap(_.toList)

      val counts = chars.groupBy(c => c).map {
        case (char, list) => CharCountPair(char, list.size)
      }
      counts.toSeq.sortWith(_.count > _.count)
    }
  }

  override def predictDuplicates(words: Future[List[String]]): Future[Seq[List[String]]] = ???
}
