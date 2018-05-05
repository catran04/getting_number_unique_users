package com.catran.database.my_sql

import java.sql.Connection

import com.catran.options.{ApplicationOptions, MysqlOptions}
import org.apache.log4j.Logger

/**
  * Created by Administrator on 5/5/2018.
  */
class MySqlTrainee(cn: Connection, options: ApplicationOptions) {

  private val logger = Logger.getLogger(getClass)
  private val statement = cn.createStatement()

  /**
    * the method for using concrete database.
    */
  def useDB: Unit = {
    statement.execute(s"USE ${options.mysql.databaseName};")
    logger.info(s"database ${options.mysql.databaseName} is using")
  }

  /**
    * creates the table for a working application
    */
  def createTable: Unit = {
    statement.execute(s"CREATE TABLE IF NOT EXISTS ${options.userTableName}(" +
      "id VARCHAR(128) PRIMARY KEY);")
    logger.info(s"the table '${options.userTableName}' was created")
  }
}

object MySqlTrainee {

  /**
    * change database and creates table into it
    * @param cn connection to MySql server
    * @param options
    */
  def apply(cn: Connection, options: ApplicationOptions): Unit = {
    val trainee = new MySqlTrainee(cn, options)
    trainee.useDB
    trainee.createTable
  }
}