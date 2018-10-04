package services.charCounter

import models.CharCount.CharCountPair

import scala.concurrent.Future

trait CharService {
  def genCharCount(words: Future[List[String]]): Future[Seq[CharCountPair]]
}
