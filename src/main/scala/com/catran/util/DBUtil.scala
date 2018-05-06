package com.catran.util

import com.catran.dao.user.{InMemoryUserDao, MySqlUserDao, SqLiteUserDao, UserDao}
import com.catran.database.my_sql.MySqlConnector
import com.catran.database.sq_lite.SQLiteConnector
import com.catran.options.ApplicationOptions

/**
  * this method is receiving option and returns the implementation of Dao
  */
object DBUtil {
  def apply(options: ApplicationOptions, testConnection: Boolean): UserDao = {
    options.storage match {
      case "MySql" => new MySqlUserDao(options, MySqlConnector)
      case "SqLite" => new SqLiteUserDao(options, new SQLiteConnector, testConnection)
      case "Memory" => new InMemoryUserDao
      case otherString => throw new IllegalArgumentException(s"using userStorage ${otherString} is impossible")
    }
  }
}