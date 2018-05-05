package com.catran.dao.user

/**
  * Created by Administrator on 5/5/2018.
  */
trait UserDao {

  def isExist(userId: String): Boolean
  def addUser(userId: String): Unit
  def reset: Unit

  def handleExceptions[T](block: () => T): T = try {
    block()
  } catch {
    case e: Exception => throw new DaoException(e)
  }

}
