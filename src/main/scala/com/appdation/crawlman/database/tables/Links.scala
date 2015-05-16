package com.appdation.crawlman.database.tables

import scala.slick.driver.MySQLDriver.simple._

final case class DbLink(source: Long, target: Long)

final class Links(tag: Tag) extends Table[DbLink](tag, "links") {

  def source = column[Long]("source")
  def target = column[Long]("target")

  def * = (source, target) <> (DbLink.tupled, DbLink.unapply)

}
