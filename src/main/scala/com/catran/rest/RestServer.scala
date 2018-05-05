package com.catran.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.catran.dao.user.MySqlUserDao
import com.catran.database.my_sql.MySqlConnector
import com.catran.options.{ApplicationContext, ApplicationOptions}
import org.apache.log4j.Logger
import org.joda.time.DateTimeZone

import scala.concurrent.Future
import scala.io.StdIn


/**
  * the start point of application.
  */
class RestServer(applicationContext: ApplicationContext) {

  private val logger: Logger = Logger.getLogger(getClass)


  /**
    * creates the connection to database and launch REST server
    */
  def launch(): Unit = {
    DateTimeZone.setDefault(DateTimeZone.UTC)
    val options = applicationContext.options

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route = RestRoute(applicationContext)
    val host = options.rest.host
    val port = options.rest.port

    val serverBinding: Future[ServerBinding] = Http().bindAndHandle(route.getRoute, host, port)
    logger.info(s"REST server is now listening on http://$host:$port/...\nYou may shutdown server if will print something in console")

    StdIn.readLine() // let it run until user presses return

    applicationContext.userDao.close //close the connection to database

    serverBinding
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when do

    logger.info("REST server was shutdown")
  }
}

object RestServer {
  private val logger = Logger.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    val applicationContext = initializeContext(args)
    val app = new RestServer(applicationContext)
    app.launch()
  }

  private def initializeContext(args: Array[String]): ApplicationContext = {
    logger.info(s"Parsing input arguments: '${args.mkString(" ")}'")
    val applicationOptions = ApplicationOptions(args)
    logger.info(s"options ${applicationOptions}")

    ApplicationContext(
      options = applicationOptions,
      userDao = new MySqlUserDao(applicationOptions, MySqlConnector)
    )
  }
}