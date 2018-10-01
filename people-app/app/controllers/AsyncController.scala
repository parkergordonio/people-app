package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.libs.json.Json
import play.api.mvc._
import services.people.PeopleService

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param cc standard controller components
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.  When rendering content, you should use Play's
 * default execution context, which is dependency injected.  If you are
 * using blocking operations, such as database or network access, then you should
 * use a different custom execution context that has a thread pool configured for
 * a blocking API.
 */
@Singleton
class AsyncController @Inject()(
    cc: ControllerComponents,
    actorSystem: ActorSystem,
    peopleService: PeopleService)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  import models.people.Person.personWrites

  def message = Action.async {
    getFutureMessage(1.second).map { msg => Ok(msg) }
  }

  def findAllPeople = Action.async {
    peopleService.findAll().map { p =>
      Ok(Json.toJson(p))
    }
//      .recover {
//      case ex => InternalServerError("There was a failure")
//    }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

}
