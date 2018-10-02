package services.people

import models.people.{PeoplePage, Person}

import scala.concurrent.Future



trait PeopleService {
  def find(page: Int, pageSize: Int): Future[PeoplePage]
  //def findAll(): Future[List[Person]]
}