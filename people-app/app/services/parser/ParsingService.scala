package services.parser

import models.charCount.CharCountPair

import scala.concurrent.Future

trait ParsingService {
  def lookupCharFrequency(words: Future[List[String]]): Future[Seq[CharCountPair]]
  def possibleDuplicates(words: Future[List[String]]): Future[Seq[List[String]]]
}
