package com.catran.rest



import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server
import akka.http.scaladsl.server.{Directives, StandardRoute}
import com.catran.handler.UserHandler
import com.catran.options.ApplicationContext

/**
  * Routing for REST API
  */
class RestRoute(appContext: ApplicationContext) extends Directives {

  private val userHandler = new UserHandler(appContext.userDao)

  /**
    * the routing of the user requests
    */
  def getRoute: server.Route = synchronized{
      path("user") synchronized{
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
          } ~
          delete {
            val response = userHandler.reset
            response.error_message match {
              case None => processSuccess(response.message.get)
              case Some(err_msg) => processFailure(err_msg, response.response_code)
            }
          }
        }
  }

  /**
    * handles success response
    * @param response - the answer for request from the user
    */
  private def processSuccess(response: String): StandardRoute = {
    complete(StatusCodes.OK, response)
  }

  /**
    * handles response with some error
    * @param errorMessage - defines the message about error
    * @param responseCode - http code
    */
  private def processFailure(errorMessage: String, responseCode: Int): StandardRoute = {
    responseCode match {
      case StatusCodes.BadRequest.intValue => complete(StatusCodes.BadRequest.intValue, errorMessage)
      case StatusCodes.NotFound.intValue => complete(StatusCodes.NotFound.intValue, errorMessage)
      case _ => complete(StatusCodes.InternalServerError.intValue, errorMessage)
    }
  }
}

object RestRoute {
  def apply(appContext: ApplicationContext): RestRoute = new RestRoute(appContext)
}