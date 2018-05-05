package com.catran.model

import akka.http.scaladsl.model.StatusCodes
import org.json4s.native.Serialization.write
import org.json4s.{NoTypeHints, native}

/**
  * defines the response to user
  */
case class Response(
                   response_code: Int = StatusCodes.OK.intValue,
                   error_message: Option[String] = None,
                   message: Option[String] = None,
                   number_unique_users: Option[Long] = None
                   ) {
  override def toString: String = {
    implicit val formats = native.Serialization.formats(NoTypeHints)
    write[Response](this)
  }
}
