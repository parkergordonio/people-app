package services.people

import javax.inject.Inject

import models.charCount.CharCountPair
import models.duplicates.EmailDuplicates
import models.people.{PageMeta, PeoplePage, Person}
import play.api.Configuration
import play.api.libs.ws.{WSClient, WSResponse}
import services.parser.EmailParser.Email
import services.parser.{EmailParser, ParsingService}

import scala.concurrent.{ExecutionContext, Future}


class SalesLoftPeopleService @Inject() (
    ws: WSClient,
    emailUtil: ParsingService,
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

  private def getAllEmails() = {
    val pageSize = 10
    val totalPages = getTotalPages(pageSize)
    val people = peopleFromPageCount(totalPages, pageSize)
    onlyEmail(people)
  }

  override def findEmailCharFrequency(): Future[Seq[CharCountPair]] = {
    val emails = getAllEmails()
    emailUtil.lookupCharFrequency(emails)
  }

  override def findEmailDuplicates(): Future[EmailDuplicates] = {
    val emails = getAllEmails()
    val dupes = emailUtil.possibleDuplicates(emails)

    dupes.map { d =>
      EmailDuplicates(d.size, d)
    }
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
}
