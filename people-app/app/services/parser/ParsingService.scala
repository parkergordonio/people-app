package services.parser

import models.CharCount.CharCountPair

import scala.concurrent.Future

trait ParsingService {
  def lookupCharFrequency(words: Future[List[String]]): Future[Seq[CharCountPair]]
  def predictDuplicates(words: Future[List[String]]): Future[Seq[List[String]]]
}
