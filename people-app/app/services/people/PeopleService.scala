package services.people

import models.CharCount.CharCountPair
import models.people.{PeoplePage, Person}

import scala.concurrent.Future



trait PeopleService {
  def find(page: Int, pageSize: Int): Future[PeoplePage]
  def findCharFrequency(): Future[Seq[CharCountPair]]
}