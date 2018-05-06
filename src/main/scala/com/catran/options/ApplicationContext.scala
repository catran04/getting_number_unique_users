package com.catran.options

import java.io.File

import com.catran.dao.user.{InFileMemoryUserDao, UserDao}

/**
  * contains the different info for using application. Maybe to change
  */
case class ApplicationContext(
                               options: ApplicationOptions,
                               userDao: UserDao
                             )
object ApplicationContext {
  def getInFileMemoryUserDao: UserDao = {
    createStorageFile
    Thread.sleep(10000)
    new InFileMemoryUserDao
  }

  private def createStorageFile: Unit = {
    val resourceFileStorage = s"src${File.separator}main${File.separator}resources${File.separator}dao${File.separator}in_file_memory_user_storage.txt"
    val file = new File(resourceFileStorage)
    file.getParentFile.mkdirs()
    file.createNewFile()
  }
}