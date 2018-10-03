package utils

import javax.inject.Inject

import models.CharCount.CharCountPair

import scala.concurrent.{ExecutionContext, Future}


trait CharUtil {
  def genCharCount(words: Future[List[String]]): Future[Seq[CharCountPair]]
}


class CharCounter @Inject() (implicit val ex: ExecutionContext) extends CharUtil {
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