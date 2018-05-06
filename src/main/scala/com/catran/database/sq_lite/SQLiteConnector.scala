package com.catran.database.sq_lite

import java.sql.{Connection, DriverManager}

import com.catran.database.SQLConnector
import com.catran.options.ApplicationOptions
import org.apache.log4j.Logger

class SQLiteConnector extends SQLConnector {

  private val logger = Logger.getLogger(getClass)
  private var url: String = _

  /**
    * creates the connection to a SqLite
    *
    * @param appOpt : ApplicationOptions - the options for a handling of connection
    * @return Connection - the connection to the SqLite
    */
  override def getConnection(appOpt: ApplicationOptions, testConnection: Boolean): Connection = {
    val options = appOpt.sqlite

    if (testConnection) url = appOpt.sqlite.testConnection
    else url = appOpt.sqlite.workingConnection

    val driver = options.driver
    Class.forName(driver)
    val connection = DriverManager.getConnection(url)
    logger.info(s"connection to database test was successed")
    connection
  }
}
