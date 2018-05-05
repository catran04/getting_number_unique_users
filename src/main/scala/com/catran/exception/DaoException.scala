package com.catran.exception

/**
  * creates for handling of the bad results of accessing to a storage
  */
class DaoException(message: String) extends Exception(message){

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
