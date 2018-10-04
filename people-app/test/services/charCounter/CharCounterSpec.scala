package services.charCounter

import akka.actor.ActorSystem
import org.scalatest.AsyncFlatSpec
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test.FakeRequest

import scala.concurrent.Future


class CharCounterSpec extends AsyncFlatSpec {

  val emails = List("t@aol.com", "p@aol.com")

  "CharCounterService" should "get a list of the characters and their occurence frequency" in {
       val counter = new CharCounter()
       val counts = counter.genCharCount(Future(emails))
       counts.map { chars =>
         assert(chars.size == 9)
         assert(chars.find(_.char == 'a').get.count == 2)
       }
  }
}
