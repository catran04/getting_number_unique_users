package com.catran.dao.user

import com.catran.database.SQLConnector
import com.catran.exception.DaoException
import com.catran.options.ApplicationOptions
import org.apache.log4j.Logger

import scala.collection.mutable

abstract class AbstractSqlUserDao(appOptions: ApplicationOptions, connector: SQLConnector) extends UserDao {

  private val connection = connector.getConnection(appOptions)
  private val statement = connection.createStatement()
  private val logger = Logger.getLogger(getClass)

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
    * requests to a storage and returns the number of unique users
    */
  override def getUniqueNumber: Long = {
    try {
      val query = s"SELECT COUNT(*) FROM ${appOptions.userTableName};"
      val rs = statement.executeQuery(query)
      if(rs.next()) {
        rs.getLong(1)
      } else
        throw new DaoException("The getting of unique number of Users was failure")
    } catch {
      case e: DaoException => throw new DaoException(e)
      case e: Exception => throw new DaoException(e)
    }
  }

  /**
    * closes the connection to a MySql server
    */
  override def close: Unit = {
    connection.close()
    logger.info("Connection to SqLite is closed")
  }
}
