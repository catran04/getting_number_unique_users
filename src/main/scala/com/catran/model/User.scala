package com.catran.model

import com.catran.exception.IllegalRequestException
import org.json4s.{NoTypeHints, native}
import org.json4s.native.Serialization.{read, write}

import scala.util.{Failure, Success, Try}

/**
  * defines a body of a post request from an user
  */
case class User(user_id: String)

object User {
  def fromJson(json: String): User = {
    implicit val formats = native.Serialization.formats(NoTypeHints)

    Try(read[User](json)) match {
      case Success(value) => value
      case Failure(e) => throw new IllegalRequestException(e)
    }
  }
}