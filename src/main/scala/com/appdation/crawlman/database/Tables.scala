package com.appdation.crawlman.database

import scala.slick.driver.MySQLDriver.simple._

trait Tables {

  val tables = new Tables()

  class Tables {

    val pages = TableQuery[Pages]
    val links = TableQuery[Links]

  }

}
