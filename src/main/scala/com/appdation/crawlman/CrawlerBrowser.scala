package com.appdation.crawlman

import java.security.MessageDigest

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConversions._

case class CrawlerBrowser(requestUrl: String) {

  private lazy val document = getPageDocument()

  private def getPageDocument(): Option[Document] = {
    try {
      Some(Jsoup.connect(requestUrl).get())
    } catch {
      case e: Exception => None
    }
  }

  def getTitle(): Option[String] = {
    document.map(_.title())
  }

  def getHtml(): Option[String] = {
    document.map(_.html())
  }

  def getHtmlDigest(): Option[String] = {
    getHtml().map(s => new String(MessageDigest.getInstance("MD5").digest(s.getBytes())))
  }

  def getLinks(): List[String] = {
    document match {
      case Some(d) => {
        val links = d.select("a[href]")
        links.subList(0, links.size() - 1).toList.map(_.attr("abs:href"))
      }
      case _ => List()
    }
  }

}
