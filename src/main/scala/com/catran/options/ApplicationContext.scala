package com.catran.options

import com.catran.dao.user.UserDao

/**
  * contains the different info for using application. Maybe to change
  */
case class ApplicationContext(
                               options: ApplicationOptions,
                               userDao: UserDao
                             )