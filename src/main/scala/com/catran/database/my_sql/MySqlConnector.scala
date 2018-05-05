package com.catran.database.my_sql

import java.sql.{Connection, DriverManager}

import com.catran.database.SQLConnector
import com.catran.options.ApplicationOptions
import org.apache.log4j.Logger

object MySqlConnector extends SQLConnector {

  private val logger = Logger.getLogger(getClass)

  /**
    * creates the connection to a MySQL server
    * @param appOpt: ApplicationOptions - the options for a handling of connection
    * @return Connection - the connection to the MySQL server
    */
  override def getConnection(appOpt: ApplicationOptions): Connection = {
    val options = appOpt.mysql
    val driver = options.driver

    val host = options.host
    val port = options.port
    val databaseName = options.userDatabaseName
    val autoReconnect = options.autoReconnect
    val useSSL = options.useSSL
    val useJDBCCompliantTimezoneShift = options.useJDBCCompliantTimezoneShift
    val useLegacyDatetimeCode = options.useLegacyDatetimeCode
    val serverTimezone = options.serverTimezone


    val url = s"jdbc:mysql://${host}:${port}/${databaseName}" +
      s"?autoReconnect=${autoReconnect}&useSSL=${useSSL}&useJDBCCompliantTimezoneShift=${useJDBCCompliantTimezoneShift}" +
      s"&useLegacyDatetimeCode=${useLegacyDatetimeCode}&serverTimezone=${serverTimezone}"
    println(s"url ${url}")

    val username = options.username
    val password = options.password

    Class.forName(driver)
    val connection = DriverManager.getConnection(url, username, password)
    logger.info(s"connection to database ${databaseName} was success")
    connection
  }
}