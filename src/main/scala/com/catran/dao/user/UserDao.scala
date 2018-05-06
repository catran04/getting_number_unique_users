package com.catran.dao.user

/**
  * defines methods for working with user requests
  */
trait UserDao {
  def addUser(userId: String): Unit
  def getAllUniqueUsers: collection.mutable.HashSet[String]
  def getUniqueNumber: Long
  def reset(): Unit
  def close(): Unit
}
