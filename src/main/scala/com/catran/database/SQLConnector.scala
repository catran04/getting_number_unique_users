package com.catran.database

import java.sql.Connection

import com.catran.options.ApplicationOptions

/**
  * defines the connection to the MySQL server
  */
trait SQLConnector {

  def getConnection(options: ApplicationOptions): Connection
}