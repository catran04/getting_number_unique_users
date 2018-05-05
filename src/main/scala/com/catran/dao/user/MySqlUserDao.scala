package com.catran.dao.user

import com.catran.database.SQLConnector
import com.catran.database.my_sql.MySqlTrainee
import com.catran.exception.DaoException
import com.catran.options.ApplicationOptions
import org.apache.log4j.Logger

import scala.collection.mutable

/**
  * serves as a layer between the UserHandler and the mySql database
  */
class MySqlUserDao(appOptions: ApplicationOptions, connector: SQLConnector) extends UserDao{

  private val connection = connector.getConnection(appOptions)
  private val statement = connection.createStatement()
  private val logger = Logger.getLogger(getClass)
  MySqlTrainee(connection, appOptions) //prepare database and creates table for the using

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
    * checks of existing an user in a storage
    * serves for an integration tests
    */
  override def isExist(userId: String): Boolean = {
    try {
      val query = s"SELECT EXIST" +
        s"(SELECT * FROM ${appOptions.userTableName}" +
        s" WHERE id = ${userId}" +
        s" LIMIT 1;)"
    val rs = statement.executeQuery(query)
    rs.getBoolean(1)
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
    * added new user into a storage
    */
  override def addUser(userId: String): Unit = {
    try {
      val query = s"INSERT INTO ${appOptions.userTableName} (id) VALUES (?);"
      val ps = connection.prepareStatement(query)
      ps.setString(1, userId)
      if(!ps.execute()) throw new DaoException(s"The adding user: '${userId}' was failure")
    } catch {
      case e: DaoException => throw new DaoException(e)
      case e: Exception => throw new DaoException(e)
    }
  }

  /**
    * deletes all rows from a table
    */
  override def reset: Unit = {
    try{
      statement.execute(s"DELETE FROM ${appOptions.userTableName};")
    } catch {
      case e: Exception => throw new DaoException(e)
    }
  }

  /**
    * closes the connection to a MySql server
    */
  override def close: Unit = {
    connection.close()
    logger.info("Connection to MySql is closed")
  }
}
