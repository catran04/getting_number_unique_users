package com.catran.handler

import akka.http.scaladsl.model.StatusCodes
import com.catran.dao.user.UserDao
import com.catran.exception.{DaoException, IllegalRequestException}
import com.catran.model.{Response, User}
import org.apache.log4j.Logger

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
  * handles the user requests. Uses UserDao for a storage
  */
class UserHandler(userDao: UserDao) {
  private val logger = Logger.getLogger(getClass)

  private var localStorage = userDao.getAllUniqueUsers

  private val INTERNAL_ERROR_MESSAGE = "Wow you found a vulnerability in my code. And this means that I failed" +
  " the test task. It's very sad. I wanted to work for you"
  val NON_FIRST_VISIT_MESSAGE = ", how many times do you want to come here?"
  val FIRST_VISIT_MESSAGE = s", hello. Nice to meet you."

  /**
    * updates the counter of unique users to 0
    * @return response with message about success updating if handling of request was success
    */
  def reset: Response = synchronized{
    try{
      userDao.reset()
      localStorage = mutable.HashSet[String]()
      logger.info("updating the counter of unique users to 0")
      Response(message = Some("counter is 0"))
    } catch {
      case e: DaoException =>
        logger.error(e.getMessage,e)
        respondFailure(StatusCodes.InternalServerError.intValue, StatusCodes.InternalServerError.defaultMessage)
      case e: Exception =>
        logger.error(e.getMessage, e)
        respondFailure(StatusCodes.InternalServerError.intValue, INTERNAL_ERROR_MESSAGE)
    }
  }

  /**
    * @return the number of unique users
    */
  def getUniqueNumberUser: Response = synchronized {
    try{
      logger.info(s"getting the unique number of the users: ${localStorage.size}")
      Response(number_unique_users = Some(localStorage.size))
    } catch {
      case e: DaoException =>
        logger.error(e.getMessage,e)
        respondFailure(StatusCodes.InternalServerError.intValue, StatusCodes.InternalServerError.defaultMessage)
      case e: Exception =>
        logger.error(e.getMessage, e)
        respondFailure(StatusCodes.InternalServerError.intValue, INTERNAL_ERROR_MESSAGE)
    }
  }

  /**
    * adding new user into a storage and localStorage id it was first request and returns response
    * else returns funny message
    * @param request contains info about an user
    * @return response with a message about information of unique request
    */
  def userHandle(request: String): Response = synchronized{
    try {
      val userId = parse(request).user_id
      logger.info(s"handling request from a user: ${userId}")
      val numberUniqueUsers = localStorage.size
      addUserToLocal(userId)

      if (numberUniqueUsers == localStorage.size) synchronized{
        logger.info(s"the user: ${userId} already was requested")
        Response(message = Some(userId + NON_FIRST_VISIT_MESSAGE))
      } else synchronized{
        logger.info(s"adding a new user: ${userId} to a storage")
        Try(userDao.addUser(userId)) match {
          case Success(_) =>
            logger.info("the user was added into a storage")
            Response(message = Some(userId + FIRST_VISIT_MESSAGE))
          case Failure(e) =>
            logger.info("adding a new user was failed")
            localStorage -= userId
            throw new DaoException(e)
        }
      }
    } catch {
      case e: IllegalRequestException =>
        logger.error(e.getMessage, e)
        respondFailure(StatusCodes.BadRequest.intValue, e.getMessage)
      case e: DaoException =>
        logger.error(e.getMessage, e)
        respondFailure(StatusCodes.InternalServerError.intValue, StatusCodes.InternalServerError.defaultMessage)
      case e: Exception =>
        logger.error(e.getMessage, e)
        respondFailure(StatusCodes.InternalServerError.intValue, INTERNAL_ERROR_MESSAGE)
    }
  }

  /**
    * parses json to User
    * @param json from the body of the request
    * @return case class User
    */
  private def parse(json: String): User = {
    User.fromJson(json)
  }

  private def addUserToLocal(userId: String): Unit = synchronized{
    localStorage += userId
  }

  /**
    * @param statusCode Http code
    * @param errorMessage - message about non normal handling of request
    * @return response that contains about non normal handling of request
    */
  private def respondFailure(statusCode: Int, errorMessage: String): Response = {
    Response(
      response_code = statusCode,
      error_message = Some(errorMessage)
    )
  }
}
