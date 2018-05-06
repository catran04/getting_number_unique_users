package com.catran.dao.user
import scala.collection.mutable

class InMemoryUserDao extends UserDao{
  private var users = mutable.HashSet[String]()

  override def addUser(userId: String): Unit = {
    users += userId
  }

  override def getUniqueNumber: Long = {
    users.size
  }

  override def getAllUniqueUsers: mutable.HashSet[String] = {
    users
  }

  override def reset: Unit = {
    users = mutable.HashSet[String]()
  }

  override def close: Unit = {}
}
