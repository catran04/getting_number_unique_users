package com.catran.dao.user

import com.catran.database.SQLConnector
import com.catran.database.my_sql.MySqlTrainee
import com.catran.options.ApplicationOptions
import org.apache.log4j.Logger

import scala.util.{Failure, Success, Try}

/**
  * Created by Administrator on 5/5/2018.
  */
class MySqlUserDao(appOptions: ApplicationOptions, connector: SQLConnector) extends UserDao{

  private val connection = connector.getConnection(appOptions)
  private val statement = connection.createStatement()
  private val logger = Logger.getLogger(getClass)
  MySqlTrainee(connection, appOptions)


  override def isExist(userId: String): Boolean = {
    val rs = statement.executeQuery(s"SELECT EXIST" +
      s"(SELECT * FROM ${appOptions.userTableName}" +
      s" WHERE id = ${userId}" +
      s" LIMIT 1;)")
    Try(rs.getBoolean(1)) match {
      case Success(value) => value
      case Failure(e) => throw new DaoException(e)
    }
  }

  override def getUniqueNumber: Long = {
    try {
      val rs = statement.executeQuery(s"SELECT COUNT(*) FROM ${appOptions.userTableName};")
     rs.getLong(1)
    } catch {
      case e: Exception => throw new DaoException(e)
    }
  }

  override def addUser(userId: String): Unit = {
    try {
      val ps = connection.prepareStatement(s"INSERT INTO ${appOptions.userTableName} (id) VALUES (?);")
      ps.setString(1, userId)
    } catch {
      case e: Exception => throw new DaoException(e)
    }
  }

  override def reset: Unit = {
    try{
      statement.execute(s"DELETE FROM ${appOptions.userTableName};")
    } catch {
      case e: Exception => throw new DaoException(e)
    }
  }


}
