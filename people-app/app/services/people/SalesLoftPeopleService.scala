package services.people

import javax.inject.Inject

import models.CharCount.CharCountPair
import models.people.{PageMeta, PeoplePage, Person}
import play.api.Configuration
import play.api.libs.ws.{WSClient, WSResponse}
import services.charCounter.{CharService}

import scala.concurrent.{ExecutionContext, Future}


class SalesLoftPeopleService @Inject() (
    ws: WSClient,
    charUtil: CharService,
    config: Configuration,
    implicit val ec: ExecutionContext) extends PeopleService {

  val SALESLOFT_API_URL = config.get[String]("api.salesLoft.url")
  val token = config.get[String]("api.salesLoft.apiKey")

  val authHeader = s"Bearer ${token}"
  val baseRequest = ws.url(SALESLOFT_API_URL).addHttpHeaders("Authorization" -> authHeader)

  private def toFuturePeopleSeq(peopleRequest: Seq[Future[Seq[Person]]]): Future[Seq[Person]] = {
    val invertFut = Future.sequence(peopleRequest)
    invertFut.map(_.flatten)
  }

  private def collectPeople(pages: Int, pageSize: Int): Future[Seq[Person]] = {
    def pageToPersonRequest(p: Int): Future[Seq[Person]] = find(p, pageSize).map(_.people)
    val peopleRequest: Seq[Future[Seq[Person]]] = (1 to pages).map(pageToPersonRequest)
    toFuturePeopleSeq(peopleRequest)
  }

  private def peopleFromPageCount(pageCount: Future[Int], pageSize: Int): Future[Seq[Person]] = {
    pageCount.map(p => collectPeople(p, pageSize)).flatten
  }

  private def onlyEmail(people: Future[Seq[Person]]): Future[List[String]] = {
    people.map(_.map(_.email).toList)
  }

  override def findCharFrequency(): Future[Seq[CharCountPair]] = {
    val pageSize = 10
    val totalPages = getTotalPages(pageSize)
    val people = peopleFromPageCount(totalPages, pageSize)
    val emails = onlyEmail(people)
    charUtil.genCharCount(emails)
  }

  override def find(page: Int, pageSize: Int): Future[PeoplePage] = {
    val requestPagingInfoParams = ("include_paging_counts", "true")
    val perPageParam = ("per_page", pageSize.toString)
    val pageParam = ("page", page.toString)
    val requestWithQuery = baseRequest.withQueryStringParameters(perPageParam, pageParam, requestPagingInfoParams)

    requestWithQuery.get().map { response =>
      (response.json).as[PeoplePage]
    }
  }

  def getTotalPages(pageSize: Int): Future[Int] = {
    val authHeader = s"Bearer ${token}"
    val request = ws.url(SALESLOFT_API_URL).addHttpHeaders("Authorization" -> authHeader)
    val requestWithQuery = request.withQueryStringParameters(("include_paging_counts", "true"))

    requestWithQuery.get().map { response =>
      (response.json \ "metadata" \ "paging" \ "total_pages").as[Int]
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
