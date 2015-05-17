package com.appdation.crawlman.database

import scala.slick.driver.MySQLDriver.simple._

case class DbLink(source: Long, target: String)

class Links(tag: Tag) extends Table[DbLink](tag, "links") {

  def source = column[Long]("source")
  def target = column[String]("target")

  def * = (source, target) <> (DbLink.tupled, DbLink.unapply)

}
