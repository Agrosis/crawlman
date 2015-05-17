package com.appdation.crawlman.services

import com.appdation.crawlman.database.{DbPage, DbLink}

import scala.slick.driver.MySQLDriver.simple._

final case class CrawlerServices(private val database: DatabaseService) {

  def addPage(link: String, title: String, contentDigest: String): Long = database.database { implicit session =>
    (database.tables.pages returning database.tables.pages.map(_.id)) += (DbPage(0, link, title, contentDigest))
  }

  def addLink(source: Long, target: String) = database.database { implicit session =>
    database.tables.links.insert(DbLink(source, target))
  }

  def pageExists(link: String): Boolean = database.database { implicit session =>
    database.tables.pages.filter(_.title === link).exists.run
  }

}
