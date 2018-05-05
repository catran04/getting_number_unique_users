package com.catran.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.apache.log4j.Logger
import org.joda.time.DateTimeZone


/**
  * the start point of application.
  */
class ScalaRestBind (applicationContext: ApplicationContext){

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

    Http().bindAndHandle(route.route, host, port)
    logger.info(s"REST server is now listening on http://$host:$port/...")
  }
}

object ScalaRestBind {

  def main(args: Array[String]): Unit = {
    println(s"Parsing input arguments: '${args.mkString(" ")}'")
    val applicationOptions = ApplicationOptions(args)
    println(s"options ${applicationOptions}")
    val storage = DBUtil(applicationOptions)
    println(s"storage ${storage}")
    storage.addTimeData(10000)

    val applicationContext = ApplicationContext(
      options = applicationOptions,
      storage = storage
    )

    val app = new ScalaRestBind(applicationContext)
    app.launch()
  }
}