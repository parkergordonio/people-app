package services.parser

import org.scalatest.AsyncFlatSpec
import scala.concurrent.Future


class EmailParserSpec extends AsyncFlatSpec {
  "EmailParser" should "get a list of the characters and their occurrence frequency" in {
    val emails = List("t@aol.com", "p@aol.com")

    val counter = new EmailParser()
    val counts = counter.lookupCharFrequency(Future(emails))
    counts.map { chars =>
      assert(chars.size == 9)
      assert(chars.find(_.char == 'a').get.count == 2)
      assert(chars.find(_.char == 'p').get.count == 1)
    }
  }

  it should "determine possible duplicate emails with a heavy right sided similarity" in {
    val dupedEmails = List("pparker@gmail.com", "parkparker@gmail.com", "parker@gmail.com")
    val emails = "fakenondupe@gmail.com" :: dupedEmails
    val emailInput = Future(emails)

    val parser = new EmailParser()
    val dupes = parser.lookupCharFrequency(emailInput)
    dupes.map { d =>
      assert(d == emails)
    }
  }

  it should "determine possible duplicate emails with a heavy left sided similarity" in {
    val dupedEmails = List("tonys@gmail.com", "tonystark@gmail.com", "tony.stark@gmail.com")
    val emails: List[String] = "fakenondupe@gmail.com" :: dupedEmails
    val emailInput = Future(emails)

    val parser = new EmailParser()
    val dupes = parser.predictDuplicates(emailInput)
    dupes.map { d =>
      assert(d == dupedEmails)
    }
  }
}
