package services.charCounter

import javax.inject.Inject

import models.CharCount.CharCountPair

import scala.concurrent.{ExecutionContext, Future}

class CharCounter @Inject() (implicit val ex: ExecutionContext) extends CharService {
  override def genCharCount(wordsFut: Future[List[String]]): Future[Seq[CharCountPair]] = {
    wordsFut.map { words =>
      val chars = words.flatMap(_.toList)

      val counts = chars.groupBy(c => c).map {
        case (char, list) => CharCountPair(char, list.size)
      }
      counts.toSeq.sortWith(_.count > _.count)
    }
  }
}
