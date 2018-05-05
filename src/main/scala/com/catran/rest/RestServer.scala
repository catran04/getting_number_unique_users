package com.catran.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.catran.dao.user.MySqlUserDao
import com.catran.database.my_sql.MySqlConnector
import com.catran.options.{ApplicationContext, ApplicationOptions}
import org.apache.log4j.Logger
import org.joda.time.DateTimeZone


/**
  * the start point of application.
  */
class RestServer(applicationContext: ApplicationContext) {

  private val logger: Logger = Logger.getLogger(getClass)


  /**
    * creates the connection to database, adds data, and launch REST server
    */
  def launch(): Unit = {

    DateTimeZone.setDefault(DateTimeZone.UTC)

    val options = applicationContext.options

    logger.debug(s"Options applied: '${options}'")

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher // needed for the future flatMap/onComplete in the end

    val route = RestRoute(applicationContext)

    val host = options.rest.host
    val port = options.rest.port

    Http().bindAndHandle(route.getRoute, host, port)
    logger.info(s"REST server is now listening on http://$host:$port/...")
  }
}

object RestServer {

  def main(args: Array[String]): Unit = {
    val applicationContext = initializeContext(args)
    val app = new RestServer(applicationContext)
    app.launch()
  }

  private def initializeContext(args: Array[String]): ApplicationContext = {
    println(s"Parsing input arguments: '${args.mkString(" ")}'")
    val applicationOptions = ApplicationOptions(args)
    println(s"options ${applicationOptions}")

    ApplicationContext(
      options = applicationOptions,
      userDao = new MySqlUserDao(applicationOptions, MySqlConnector)
    )
  }
}