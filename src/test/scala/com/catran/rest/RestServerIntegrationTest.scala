package com.catran.rest

import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._


class RestServerIntegrationTest extends FlatSpec with GivenWhenThen with Matchers with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    //launch rest server
    RestServer.testLaunch
    //deleting old data in a storage
    val responseToDelete = Requester.sendDeleteRequest("localhost", 9080, "stat")
  }

  "request bombardier to server" should "not call incorrect working" in {
    val host = "localhost"
    val port = 9080
    val uri = "user"

    Given("request bombardier")
    for (i <- 0 until 500) {
      Requester.asynchPostRequest(host, port, uri, s"""{"user_id":"user${i}"}""")
      Requester.asynchPostRequest(host, port, uri, s"""{"user_id":"user${i}"}""")
      Thread.sleep(10) // to avoid pool overflow on client side. If on your computer throw exception put sleep more
      // (I have little time to resolve this problem)
    }

    When("waiting response using request with existing user")
    Await.result(Requester.asynchPostRequest(host, port, uri, s"""{"user_id":"user1"}"""), 1.second)

    Then("the number of unique users should be 500")
    Requester.sendGetRequest(host, port, "stat").toInt shouldBe 500

    Then("after reset should return 0 user")
    Requester.sendDeleteRequest(host, port, "stat")
  }
}
