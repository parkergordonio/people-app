package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.libs.json.Json
import play.api.mvc._
import services.people.PeopleService

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

@Singleton
class PeopleController @Inject()(
    cc: ControllerComponents,
    actorSystem: ActorSystem,
    peopleService: PeopleService)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  import models.people.Person.personWrites
  import models.people.PageMeta.pageMetaWrites

  def message = Action.async {
    getFutureMessage(1.second).map { msg => Ok(msg) }
  }

  def findPeople(page: Int, pageSize: Int) = Action.async {
    peopleService.find(page, pageSize).map { p =>
      Ok(Json.toJson(p))
    }
//      .recover {
//      case ex => InternalServerError("There was a failure")
//    }
  }

  def findCharFrequency() = Action.async {
    peopleService.findCharFrequency().map { c =>
      Ok(Json.toJson(c))
    }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

}
