package services.people

import javax.inject.Inject

import models.people.Person
import play.api.Configuration
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}


class SalesLoftPeopleService @Inject() (
    ws: WSClient,
    config: Configuration,
    implicit val ec: ExecutionContext) extends PeopleService {

  val SALESLOFT_API_URL = config.get[String]("api.salesLoft.url")
  val token = config.get[String]("api.salesLoft.apiKey")

  override def findAll(): Future[List[Person]] = {
    val authHeader = s"Bearer ${token}"
    val request = ws.url(SALESLOFT_API_URL).addHttpHeaders("Authorization" -> authHeader)

    request.get().map { response =>
      (response.json \ "data").as[List[Person]]
    }
  }

//
//  def sendRequest(): Future[WSResponse] = {
//    val authHeader = s"Bearer ${token}"
//    val request = ws.url(SALESLOFT_API_URL).addHttpHeaders("Authorization" -> authHeader)
//    request.get()
//  }

//  override def test(): Future[Person] = {
//    val authHeader = s"Bearer ${token}"
//    val request = ws.url(SALESLOFT_API_URL).addHttpHeaders("Authorization" -> authHeader)
//    request.get().map { response =>
//      val perPage = (response.json \ "metadata" \ "paging" \ "per_page").as[Int]
//      Person(perPage)
//    }
//  }
}
