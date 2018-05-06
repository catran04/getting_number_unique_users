package com.catran.dao.user

import com.catran.database.SQLConnector
import com.catran.database.my_sql.MySqlTrainee
import com.catran.exception.DaoException
import com.catran.options.ApplicationOptions
import org.apache.log4j.Logger

/**
  * serves as a layer between the UserHandler and the mySql database
  */
class MySqlUserDao(appOptions: ApplicationOptions, connector: SQLConnector) extends AbstractSqlUserDao(appOptions, connector){

  private val connection = connector.getConnection(appOptions)
  private val statement = connection.createStatement()
  private val logger = Logger.getLogger(getClass)
  MySqlTrainee(connection, appOptions) //prepare database and creates table for the using

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
}
