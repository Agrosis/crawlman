package com.appdation.crawlman.services

import com.appdation.crawlman.database.{DbPage, DbLink}

import scala.slick.driver.MySQLDriver.simple._

final case class CrawlerServices(val database: DatabaseService) {

  def addPage(link: String, title: String, contentDigest: String)(implicit session: Session): Long = {
    (database.tables.pages returning database.tables.pages.map(_.id)) += (DbPage(0, link, title, contentDigest))
  }

  def addLink(source: Long, target: String)(implicit session: Session) = {
    database.tables.links.insert(DbLink(source, target))
  }

  def pageExists(link: String)(implicit session: Session): Boolean = {
    database.tables.pages.filter(_.title === link).exists.run
  }

}
