package com.appdation.crawlman

import java.math.BigInteger
import java.security.MessageDigest

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConversions._

case class CrawlerBrowser(requestUrl: String) {

  private lazy val document = getDocument()

  private def getDocument(): Option[Document] = {
    try {
      Some(Jsoup.connect(requestUrl).timeout(3000).get())
    } catch {
      case e: Exception => None
    }
  }

  def title(): Option[String] = {
    document.map(_.title())
  }

  def html(): Option[String] = {
    document.map(_.html())
  }

  def htmlDigest(): Option[String] = {
    html().map(s => {
      val md = MessageDigest.getInstance("MD2").digest(s.getBytes())
      new BigInteger(1, md).toString(16)
    })
  }

  def links(): List[String] = {
    document match {
      case Some(d) => {
        val links = d.select("a[href]")
        if(links.size() > 0) {
          links.subList(0, links.size() - 1).toList.map(_.attr("abs:href"))
        } else List()
      }
      case _ => List()
    }
  }

}
