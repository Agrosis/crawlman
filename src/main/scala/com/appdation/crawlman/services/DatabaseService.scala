package com.appdation.crawlman.services

import java.util.Properties

import com.appdation.crawlman.database.Tables
import com.plasmaconduit.jsonconfig.JsonConfig
import com.plasmaconduit.validation.Validation
import org.flywaydb.core.Flyway
import org.fusesource.jansi.Ansi.Color._
import org.fusesource.jansi.Ansi._

import scala.slick.driver.MySQLDriver.simple._

final case class DatabaseService(driver: String, url: String, username: String, password: String) extends Tables {

  private lazy val database = Database.forURL(url, username, password, new Properties(), driver)

  def migrate(): Int = {
    println(ansi().fg(GREEN).a(s"Starting flyway migrations...").reset())

    val flyway = new Flyway()
    flyway.setDataSource(url, username, password)
    flyway.migrate()
  }

  def database[T](s: Session => T): T = {
    database.withSession { implicit session =>
      s(session)
    }
  }

}
