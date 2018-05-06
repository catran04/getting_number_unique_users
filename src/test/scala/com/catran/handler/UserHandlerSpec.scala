package com.catran.handler

import akka.http.scaladsl.model.StatusCodes
import com.catran.dao.user.InMemoryUserDao
import com.catran.model.Response
import org.scalatest.{FlatSpec, GivenWhenThen, Matchers}

class UserHandlerSpec extends FlatSpec with GivenWhenThen with Matchers {

  val userDao = new InMemoryUserDao
  val userHandler = new UserHandler(userDao)

  val user1 = """{"user_id":"user1"}"""
  val user2 = """{"user_id":"user2"}"""
  val user3 = """{"user_id":"user3"}"""

  "userHandle method" should "return response with message about first visit" in {
    Given("got response")
    val response = userHandler.userHandle(user1)

    Given("expectedResponse")
    val responseCode = StatusCodes.OK.intValue
    val responseMessage = Some("user1" + userHandler.FIRST_VISIT_MESSAGE)
    val expectedResponse = Response(response_code = responseCode, message = responseMessage)

    Then("response should equeal expectedResponse")
    response shouldEqual expectedResponse

    Then("userDao should contain 1 user")
    userDao.getUniqueNumber shouldBe 1
  }

  "userHandle method" should "return response with message about second visit" in {
    Given("got response")
    val response = userHandler.userHandle(user1)

    Given("expectedResponse")
    val responseCode = StatusCodes.OK.intValue
    val responseMessage = Some("user1" + userHandler.NON_FIRST_VISIT_MESSAGE)
    val expectedResponse = Response(response_code = responseCode, message = responseMessage)

    Then("response should equal expectedResponse")
    response shouldEqual expectedResponse

    Then("userDao should contain 1 user")
    userDao.getUniqueNumber shouldBe 1
  }

  "userHandle method" should "return responseCode 'badRequest' if json is incorrect" in {
    Given("incorrect jsons")
    val json1 = "non json"
    val json2 = """{"user_id":1}"""

    When("expected response code")
    val expectedResponseCode = StatusCodes.BadRequest.intValue

    When("response codes")
    val responseCode1 = userHandler.userHandle(json1).response_code
    val responseCode2 = userHandler.userHandle(json2).response_code

    Then("repsonse codes should equal expected response code")
    responseCode1 shouldEqual expectedResponseCode
    responseCode2 shouldEqual expectedResponseCode

    Then("userDao should contain 1 user")
    userDao.getUniqueNumber shouldBe 1
  }

}
