package com.catran.handler

import com.catran.dao.user.UserDao
import com.catran.model.{Response, User}

import scala.collection.mutable

/**
  * Created by Administrator on 5/5/2018.
  */
class UserHandler(userDao: UserDao) {

  private var localStorage: mutable.HashSet[String] = _
  localStorage = userDao.getAllUniqueUsers

  def reset: Response = {
    userDao.reset
    localStorage = mutable.HashSet[String]()
    Response(message = Some("counter is 0"))
  }

  def getUniqueNumberUser: Response = {
    Response(number_unique_users = Some(localStorage.size))
  }

  def userHandle(request: String): Response = synchronized {
    val userId = parse(request).user_id
    val numberUniqueUsers = localStorage.size
    localStorage += userId

    numberUniqueUsers == localStorage.size match {
      case true =>
        Response(message = Some(s"${userId}, how many times do you want to come here?"))
      case false =>
        userDao.addUser(userId)
        Response(message = Some(s"Hello ${userId}. Nice to meet you."))
    }
  }

  private def parse(json: String): User = {
    User.fromJson(json)
  }
}
