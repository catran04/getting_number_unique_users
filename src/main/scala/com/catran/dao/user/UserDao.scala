package com.catran.dao.user

import scala.collection.mutable

/**
  * Created by Administrator on 5/5/2018.
  */
trait UserDao {

  def isExist(userId: String): Boolean
  def addUser(userId: String): Unit
  def getUniqueNumber: Long
  def getAllUniqueUsers: mutable.HashSet[String]
  def reset: Unit
  def close: Unit


}
