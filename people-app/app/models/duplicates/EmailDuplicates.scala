package models.duplicates

import models.duplicates.EmailDupePairs.EmailDupePairs
import play.api.libs.json.{JsPath, Json, Reads, Writes}


object EmailDupePairs {
  type EmailDupePairs = List[String]

  implicit val emailDupePairsWrites = new Writes[EmailDupePairs] {
    def writes(dupes: EmailDupePairs) = Json.obj(
      "emails" -> dupes.mkString(", ")
    )
  }
}

case class EmailDuplicates(totalFound: Int, duplicatePairs: Seq[EmailDupePairs])

object EmailDuplicates {
  implicit val emailDuplicateWrites = new Writes[EmailDuplicates] {
    def writes(meta: EmailDuplicates) = Json.obj(
      "totalFound" -> meta.totalFound,
      "duplicates" -> meta.duplicatePairs.map { dupe =>
        Json.obj("emails" -> dupe.mkString(", "))
      }
    )
  }
}
