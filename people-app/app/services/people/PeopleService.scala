package services.people

import models.charCount.CharCountPair
import models.duplicates.EmailDuplicates
import models.people.{PeoplePage, Person}
import services.parser.EmailParser.Email

import scala.concurrent.Future



trait PeopleService {
  def find(page: Int, pageSize: Int): Future[PeoplePage]
  def findEmailCharFrequency(): Future[Seq[CharCountPair]]
  def findEmailDuplicates(): Future[EmailDuplicates]
}