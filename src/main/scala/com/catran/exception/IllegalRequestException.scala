package com.catran.exception

/**
  * Created by Administrator on 5/5/2018.
  */
class IllegalRequestException(message: String) extends Exception(message){

  def this(message: String, cause: Throwable) {
    this(message)
    initCause(cause)
  }

  def this(cause: Throwable) {
    this(Option(cause).map(_.toString).orNull, cause)
  }

  def this() {
    this(null: String)
  }
}
