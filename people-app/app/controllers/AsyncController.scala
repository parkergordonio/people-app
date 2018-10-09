package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.libs.json.{Json}
import play.api.mvc._
import services.people.PeopleService

import scala.concurrent.{ExecutionContext }

@Singleton
class PeopleController @Inject()(
    cc: ControllerComponents,
    actorSystem: ActorSystem,
    peopleService: PeopleService)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def findPeople(page: Int, pageSize: Int) = Action.async {
    peopleService.find(page, pageSize).map { p =>
      Ok(Json.toJson(p))
    }.recover {
      case ex => InternalServerError("Oops! There was a failure")
    }
  }

  def findCharFrequency() = Action.async {
    peopleService.findEmailCharFrequency().map { c =>
      Ok(Json.toJson(c))
    }
  }

  def findDuplicateEmails() = Action.async {
    peopleService.findEmailDuplicates().map { dupes =>
      Ok(Json.toJson(dupes))
    }
  }
}
