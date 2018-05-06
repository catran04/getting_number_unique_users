package com.catran.dao.user
import java.io._

import scala.collection.mutable

class InFileMemoryUserDao extends UserDao {
  reset

  override def isExist(userId: String): Boolean = {
    getHashSetFromFile.contains(userId)
  }

  override def addUser(userId: String): Unit = {
    writeToFile(userId, append = true)
  }

  override def getUniqueNumber: Long = {
    val users = getHashSetFromFile.map(user => user.trim)
    if(users.headOption.isEmpty || users.head == "") 0
    else users.size
  }

  override def getAllUniqueUsers: mutable.HashSet[String] = {
    getHashSetFromFile
  }

  override def reset: Unit = {
    writeToFile(message = "", append = false)
  }

  override def close: Unit = {

  }

  private def writeToFile(message: String, append: Boolean): Unit = {
    val resourceFileStorage = s"src${File.separator}main${File.separator}resources${File.separator}dao${File.separator}in_file_memory_user_storage.txt"
    val file = new File(resourceFileStorage)
    val bw =  new FileWriter(file, append)
    bw.write(message)
    bw.write(",")
    bw.flush()
    bw.close()
  }

  private def getHashSetFromFile: mutable.HashSet[String] = {
    val resourceFileStorage = s"/dao/in_file_memory_user_storage.txt"
    val source = scala.io.Source.fromURL(getClass.getResource(resourceFileStorage))
    var string = source.mkString.trim
    if(string.startsWith(",")) {
      val users = string.replaceFirst(",", "").split(",")
      mutable.HashSet[String]() ++= users.toSet
    }else {
      val users = string.split(",")
      mutable.HashSet[String]() ++= users.toSet
    }
  }
}
