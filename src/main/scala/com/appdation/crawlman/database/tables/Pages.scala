package com.appdation.crawlman.database.tables

import scala.slick.driver.MySQLDriver.simple._

final case class DbPage(id: Long, title: String, contentDigest: String)

final class Pages(tag: Tag) extends Table[DbPage](tag, "pages") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def contentDigest = column[String]("content_digest")

  def * = (id, title, contentDigest) <> (DbPage.tupled, DbPage.unapply)

}
