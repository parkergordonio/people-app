package services.people

import models.people.Person
import scala.concurrent.Future


trait PeopleService {
  def findAll(): Future[List[Person]]
}