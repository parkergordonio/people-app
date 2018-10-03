package models.CharCount

import play.api.libs.functional.syntax._
import play.api.libs.json._


case class CharCountPair(char: Char, count: Int)

object CharCountPair {
  implicit val pageMetaWrites = new Writes[CharCountPair] {
    def writes(charCount: CharCountPair) = Json.obj(
      "char" -> charCount.char.toString,
      "count" -> charCount.count
    )
  }
}