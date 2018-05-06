package com.catran.options

/**
  * using for application the different options
  */
case class ApplicationOptions(
                               storage: String = "SqLite",
                               userTableName: String = "userTable",
                               rest: RestOptions = RestOptions(),
                               mysql: MysqlOptions = MysqlOptions(),
                               sqlite: SqLiteOptions = SqLiteOptions()
                             ) {
}

object ApplicationOptions {
  val defaults = new ApplicationOptions()

  /**
    * Initialize options with given arguments
    */
  def apply(args: Array[String]): ApplicationOptions = {
    if (args.isEmpty) return ApplicationOptions.defaults
    args.foldLeft(ApplicationOptions()) { (options, arg) =>
      arg.split("=") match {
        case Array("storage", value) => options.copy(storage = value)
        case Array("userTableName", value) => options.copy(userTableName = value)

        case Array("rest.host", value) => options.copy(rest = options.rest.copy(host = value))
        case Array("rest.port", value) => options.copy(rest = options.rest.copy(port = value.toInt))
        case Array("rest.testPort", value) => options.copy(rest = options.rest.copy(testPort = value.toInt))

        case Array("mysql.host", value) => options.copy(mysql = options.mysql.copy(host = value))
        case Array("mysql.port", value) => options.copy(mysql = options.mysql.copy(port = value.toInt))
        case Array("mysql.databaseName", value) => options.copy(mysql = options.mysql.copy(databaseName = value))
        case Array("mysql.autoReconnect", value) => options.copy(mysql = options.mysql.copy(autoReconnect = value.toBoolean))
        case Array("mysql.useSSL", value) => options.copy(mysql = options.mysql.copy(useSSL = value.toBoolean))
        case Array("mysql.useJDBCCompliantTimezoneShift", value) => options.copy(mysql = options.mysql.copy(useJDBCCompliantTimezoneShift = value.toBoolean))
        case Array("mysql.useLegacyDatetimeCode", value) => options.copy(mysql = options.mysql.copy(useLegacyDatetimeCode = value.toBoolean))
        case Array("mysql.serverTimezone", value) => options.copy(mysql = options.mysql.copy(serverTimezone = value))
        case Array("mysql.driver", value) => options.copy(mysql = options.mysql.copy(driver = value))
        case Array("mysql.username", value) => options.copy(mysql = options.mysql.copy(username = value))
        case Array("mysql.password", value) => options.copy(mysql = options.mysql.copy(password = value))

        case Array("sqlite.workingConnection", value) => options.copy(sqlite = options.sqlite.copy(workingConnection = value))
        case Array("sqlite.testConnection", value) => options.copy(sqlite = options.sqlite.copy(testConnection = value))
        case Array("sqlite.driver", value) => options.copy(sqlite = options.sqlite.copy(driver = value))

        case exc => throw new RuntimeException(s"invalid args: ${exc.mkString}")
      }
    }
  }
}

case class RestOptions(
                        host: String = "localhost",
                        port: Int = 9080,
                        testPort: Int = 9081
                      )

case class MysqlOptions(
                         host: String = "127.0.0.1",
                         port: Int = 3306,
                         databaseName: String = "userdb",
                         autoReconnect: Boolean = true,
                         useSSL: Boolean = false,
                         useJDBCCompliantTimezoneShift: Boolean = true,
                         useLegacyDatetimeCode: Boolean = false,
                         serverTimezone: String = "UTC",
                         driver: String = "com.mysql.cj.jdbc.Driver",
                         username: String = "root",
                         password: String = "password"
                       )

case class SqLiteOptions(
                          workingConnection: String = "jdbc:sqlite:userdb.db",
                          testConnection: String = "jdbc:sqlite:testdb.db",
                          driver: String = "org.sqlite.JDBC"
                        )