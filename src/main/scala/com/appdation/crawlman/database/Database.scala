package com.appdation.crawlman.database

import scala.slick.driver.MySQLDriver.simple._

trait Database {

  val tables = new Tables

  private class Tables {

    val links = TableQuery[Pages]
    val pages = TableQuery[Links]

  }

}
