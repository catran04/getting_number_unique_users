package com.catran.rest



import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server
import akka.http.scaladsl.server.{Directives, StandardRoute}
import com.catran.handler.UserHandler
import com.catran.model.{Response, User}
import com.catran.options.ApplicationContext

/**
  * Routing for REST API
  */
class RestRoute(appContext: ApplicationContext) extends Directives {

  private val userHandler = new UserHandler(appContext.userDao)


  def getRoute: server.Route = {
      path("user") {
        post {
          entity(as[String]) { request =>
            val response = userHandler.userHandle(request)
            response.error_message match {
              case None => processSuccess(response.message.get)
              case Some(err_msg) => processFailure(err_msg, response.response_code)
            }
          }
        }
      } ~
        path("stat") {
          get {
            val response = userHandler.getUniqueNumberUser
            response.error_message match {
              case None => processSuccess(response.number_unique_users.get.toString)
              case Some(err_msg) => processFailure(err_msg, response.response_code)
            }
          }
          delete {
            val response = userHandler.reset
            response.error_message match {
              case None => processSuccess(response.message.get)
              case Some(err_msg) => processFailure(err_msg, response.response_code)
            }
          }
        }
  }

  private def processSuccess(responseAsJson: String): StandardRoute = {
    complete(StatusCodes.OK, responseAsJson)
  }

  private def processFailure(responseAsJson: String, responseCode: Int): StandardRoute = {
    responseCode match {
      case StatusCodes.NotFound.intValue => complete(StatusCodes.NotFound.intValue, responseAsJson)
      case _ => complete(StatusCodes.InternalServerError.intValue, responseAsJson)
    }
  }
}

object RestRoute {
  def apply(appContext: ApplicationContext): RestRoute = new RestRoute(appContext)
}