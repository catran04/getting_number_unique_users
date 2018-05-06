name := "getting_number_of_unique_users"

version := "1.0"

scalaVersion := "2.12.6"

// for creating rest server
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.1"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.11"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.11"


// for timing
libraryDependencies += "joda-time" % "joda-time" % "2.9.9"

// for a logging
libraryDependencies += "log4j" % "log4j" % "1.2.17"

// for a parsing jsons
libraryDependencies += "org.json4s" %% "json4s-native" % "3.5.2"
libraryDependencies += "org.json4s" %% "json4s-core" % "3.5.2"

// for a connection to MySql server
libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.6"

// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
