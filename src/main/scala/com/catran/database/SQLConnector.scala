package com.catran.database

import java.sql.Connection

import com.catran.options.ApplicationOptions

/**
  * defines the connection to the SQL server
  */
trait SQLConnector {

  def getConnection(options: ApplicationOptions): Connection
}