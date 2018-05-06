package com.catran.model

import com.catran.exception.IllegalRequestException
import com.catran.validator.UserSchemaValidator
import org.json4s.native.Serialization.read
import org.json4s.{NoTypeHints, native}

import scala.util.{Failure, Success, Try}

/**
  * defines a body of a post request from an user
  */
case class User(user_id: String)

object User {

  /**
    * from json to User. If json is incorrect:
    * @throws IllegalRequestException
    * @return User
    */
  def fromJson(json: String): User = {
    UserSchemaValidator().validate(json) // validation by json-schema
    implicit val formats = native.Serialization.formats(NoTypeHints)

    Try(read[User](json)) match {
      case Success(value) => value
      case Failure(e) => throw new IllegalRequestException(e)
    }
  }
}