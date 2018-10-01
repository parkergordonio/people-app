package models.people


import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Person(fullName: String, email: String, jobTitle: String)

object Person {
  implicit val personWrites = new Writes[Person] {
    def writes(person: Person) = Json.obj(
      "fullName" -> person.fullName,
      "emailAddress" -> person.email,
      "jobTitle" -> person.jobTitle
    )
  }

  implicit val personReads: Reads[Person] = (
    (JsPath \ "display_name").read[String] and
    (JsPath \ "email_address").read[String] and
    (JsPath \ "title").read[String]
  )(Person.apply _)
}