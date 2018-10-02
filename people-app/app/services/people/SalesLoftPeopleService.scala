package services.people

import javax.inject.Inject

import models.people.{PageMeta, PeoplePage, Person}
import play.api.Configuration
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}


class SalesLoftPeopleService @Inject() (
    ws: WSClient,
    config: Configuration,
    implicit val ec: ExecutionContext) extends PeopleService {

  val SALESLOFT_API_URL = config.get[String]("api.salesLoft.url")
  val token = config.get[String]("api.salesLoft.apiKey")

  val authHeader = s"Bearer ${token}"
  val baseRequest = ws.url(SALESLOFT_API_URL).addHttpHeaders("Authorization" -> authHeader)

  override def find(page: Int, pageSize: Int): Future[PeoplePage] = {
    val requestPagingInfoParams = ("include_paging_counts", "true")
    val perPageParam = ("per_page", pageSize.toString)
    val pageParam = ("page", page.toString)
    val requestWithQuery = baseRequest.withQueryStringParameters(perPageParam, pageParam, requestPagingInfoParams)

    requestWithQuery.get().map { response =>
      (response.json).as[PeoplePage]
    }
  }
//
//  override def findAll(): Future[List[Person]] = {
//    val totalCount = getTotalPersonCount()
//    val people = find(1, 100)
//    people
//  }

  def getTotalPersonCount(): Future[Int] = {
    val authHeader = s"Bearer ${token}"
    val request = ws.url(SALESLOFT_API_URL).addHttpHeaders("Authorization" -> authHeader)
    val requestWithQuery = request.withQueryStringParameters(("include_paging_counts", "true"))

    requestWithQuery.get().map { response =>
      (response.json \ "metadata" \ "paging" \ "total_count").as[Int]
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
