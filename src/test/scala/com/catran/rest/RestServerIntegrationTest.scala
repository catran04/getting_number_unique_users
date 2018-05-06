package com.catran.rest

import com.catran.dao.user.SqLiteUserDao
import com.catran.database.sq_lite.SQLiteConnector
import com.catran.options.ApplicationOptions
import org.scalatest._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


class RestServerIntegrationTest extends FlatSpec with GivenWhenThen with Matchers with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    //launch rest server
    RestServer.testLaunch
    //deleting old data in a storage
    val responseToDelete = Requester.sendDeleteRequest("localhost", 9080, "stat")
  }

  override def afterAll(): Unit = {
    val test = true
    val connector = new SQLiteConnector
    val options = ApplicationOptions.defaults
    val sqLiteUserDao = new SqLiteUserDao(options, connector, test)
    sqLiteUserDao.reset()
  }

  val host = "localhost"
  val port = 9080
  val uri = "user"
  def sendPost(json: String): Future[String] = Requester.asynchPostRequest(host, port, uri, json)

  "request bombardier to server" should "not call incorrect working" in {

    Given("request bombardier")
    for (i <- 0 until 500) {
      val response1 = sendPost(s"""{"user_id":"user${i}"}""")
      val response2 = sendPost(s"""{"user_id":"user${i}"}""")
      val response3 = sendPost(s"""{"user_id":"user${i}"}""")
      Await.result(response1, 1.second)
      Await.result(response2, 1.second)
      Await.result(response3, 1.second)
    }

    When("waiting response using request with existing user")
    Await.result(Requester.asynchPostRequest(host, port, uri, s"""{"user_id":"user1"}"""), 1.second)

    Then("the number of unique users should be 500")
    Requester.sendGetRequest(host, port, "stat").toInt shouldBe 500

    Then("after reset should return 0 user")
    Requester.sendDeleteRequest(host, port, "stat")
  }
}
