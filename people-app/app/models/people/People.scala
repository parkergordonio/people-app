package models.people


import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PageMeta(totalCount: Int, totalPages: Int, currentPage: Int)

object PageMeta {
  implicit val pageMetaWrites = new Writes[PageMeta] {
    def writes(meta: PageMeta) = Json.obj(
      "totalCount" -> meta.totalCount,
      "totalPages" -> meta.totalPages,
      "currentPage" -> meta.currentPage
    )
  }

  implicit val pageMetaReads: Reads[PageMeta] = (
    (JsPath \ "total_count").read[Int] and
      (JsPath \ "total_pages").read[Int] and
      (JsPath \ "current_page").read[Int]
    )(PageMeta.apply _)

}


case class PeoplePage(meta: PageMeta, people: Seq[Person])

object PeoplePage {

  implicit val peoplePageWrites = new Writes[PeoplePage] {
    def writes(page: PeoplePage) = Json.obj(
      "pageMeta" -> page.meta,
      "people" -> page.people
    )
  }

  implicit val peoplePageReads: Reads[PeoplePage] = (
      (JsPath \ "metadata" \ "paging").read[PageMeta] and
      (JsPath \ "data").read[Seq[Person]]
    )(PeoplePage.apply _)

}



case class Person(id: Int, fullName: String, email: String, jobTitle: String)

object Person {
  implicit val personWrites = new Writes[Person] {
    def writes(person: Person) = Json.obj(
      "fullName" -> person.fullName,
      "emailAddress" -> person.email,
      "jobTitle" -> person.jobTitle
    )
  }

  implicit val personReads: Reads[Person] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "display_name").read[String] and
    (JsPath \ "email_address").read[String] and
    (JsPath \ "title").read[String]
  )(Person.apply _)
}