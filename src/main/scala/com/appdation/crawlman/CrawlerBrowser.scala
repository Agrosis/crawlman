package com.appdation.crawlman

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConversions._

case class CrawlerBrowser(requestUrl: String) {

  private lazy val document: Document = Jsoup.connect(requestUrl).get()

  def getTitle(): String = {
    document.title()
  }

  def getHtml(): String = {
    document.html()
  }

  def getLinks(): List[String] = {
    val links = document.select("a[href]")
    links.subList(0, links.size() - 1).toList.map(_.attr("abs:href"))
  }

}
