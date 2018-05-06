package com.catran.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.catran.options.{ApplicationContext, ApplicationOptions}
import com.catran.util.DBUtil
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
  def launch(testLaunch: Boolean): Unit = {
    DateTimeZone.setDefault(DateTimeZone.UTC)
    val options = applicationContext.options

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route = RestRoute(applicationContext)
    val host = options.rest.host
    val port = options.rest.port

    val serverBinding: Future[ServerBinding] = Http().bindAndHandle(route.getRoute, host, port)
    logger.info(s"REST server is now listening on http://$host:$port/...\nYou may shutdown server if will press enter in console")

    if(!testLaunch) {
      StdIn.readLine() // let it run until user presses return

      applicationContext.userDao.close //close the connection to database

      serverBinding
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when do

      logger.info("REST server was shutdown")
    }
  }

}

object RestServer {
  private val logger = Logger.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    val applicationContext = initializeContext(args, testConnection = false)
    new RestServer(applicationContext).launch(false)
  }

  def testLaunch: Unit = {
    val applicationContext = initializeContext(testConnection = true)
    new RestServer(applicationContext).launch(true)
  }

  private def initializeContext(args: Array[String] = Array.empty, testConnection: Boolean): ApplicationContext = {
    logger.info(s"Parsing input arguments: '${args.mkString(" ")}'")
    val applicationOptions = ApplicationOptions(args)
    logger.info(s"options ${applicationOptions}")

    val userDao = DBUtil(applicationOptions, testConnection = testConnection)
    logger.info(s"using userDao: ${userDao.getClass}")

    ApplicationContext(
      options = applicationOptions,
      userDao = userDao
    )
  }
}