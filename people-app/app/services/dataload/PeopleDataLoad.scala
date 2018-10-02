package services.dataload

import play.api.cache._
import javax.inject.Inject

import play.api.Logger
import services.people.PeopleService

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

class PeopleDataLoad @Inject()(
    @NamedCache("people-cache") peopleCache: AsyncCacheApi,
    peopleService: PeopleService,
    implicit val ec: ExecutionContext) {

//  Logger.info("People Dataload Initiated")
//  val peopleRequest = peopleService.findAll()
//
//  peopleRequest.map { people =>
//    //people.foreach { p =>
//      Logger.info(s"Adding ${people.size} to the People cache")
//      peopleCache.set("people", people, Duration.Inf)
//    //}
//  }
}
