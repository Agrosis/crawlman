package com.appdation.crawlman.database

import scala.slick.driver.MySQLDriver.simple._

final case class DbPage(id: Long, link: String, title: String, contentDigest: String)

final class Pages(tag: Tag) extends Table[DbPage](tag, "pages") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def link = column[String]("link")
  def title = column[String]("title")
  def contentDigest = column[String]("content_digest")

  def * = (id, link, title, contentDigest) <> (DbPage.tupled, DbPage.unapply)

}
