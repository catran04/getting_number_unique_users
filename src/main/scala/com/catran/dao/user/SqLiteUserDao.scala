package com.catran.dao.user

import com.catran.database.SQLConnector
import com.catran.database.sq_lite.SQLiteTrainee
import com.catran.exception.DaoException
import com.catran.options.ApplicationOptions
import org.apache.log4j.Logger

import scala.collection.mutable

class SqLiteUserDao(appOptions: ApplicationOptions, connector: SQLConnector, testConnection: Boolean) extends AbstractSqlUserDao(appOptions, connector){

  private val connection = connector.getConnection(appOptions, testConnection)
  private val statement = connection.createStatement()
  private val logger = Logger.getLogger(getClass)
  SQLiteTrainee(connection, appOptions) //prepare database and creates table for the using

  /**
    * requests to mysql server and return all unique userIds
    */
  override def getAllUniqueUsers: mutable.HashSet[String] = {
    try {
      val query = s"SELECT * FROM ${appOptions.userTableName};"
      val rs = statement.executeQuery(query)
      var users: mutable.HashSet[String] = mutable.HashSet[String]()
      while (rs.next()) {
        users += rs.getString(1)
      }
      users
    } catch {
      case e: Exception => throw new DaoException(e)
    }
  }

  /**
    * added new user into a storage
    */
  override def addUser(userId: String): Unit = {
    try {
      val query = s"INSERT INTO ${appOptions.userTableName}(id) VALUES(?)"
      val ps = connection.prepareStatement(query)
      ps.setString(1, userId)
      ps.executeUpdate()
    } catch {
      case e: DaoException => throw new DaoException(e)
      case e: Exception => throw new DaoException(e)
    }
  }

  /**
    * deletes all rows from a table
    */
  override def reset(): Unit = {
    try{
      statement.execute(s"DELETE FROM ${appOptions.userTableName};")
    } catch {
      case e: Exception => throw new DaoException(e)
    }
  }
}
