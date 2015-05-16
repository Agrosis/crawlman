package com.appdation.crawlman

import java.util.concurrent.BlockingQueue
import org.fusesource.jansi.Ansi._
import org.fusesource.jansi.Ansi.Color._

import scala.xml.{Elem, XML}

case class Crawler(crawlerId: Int, workQueue: BlockingQueue[WebLink]) extends Runnable {

  override def run(): Unit = {
    println(ansi().fg(YELLOW).a(s"$crawlerId: Starting worker thread...").reset())

    while(true){
      processLink(workQueue.take())
    }
  }

  private def processLink(link: WebLink): Unit = {
    println(ansi().fg(GREEN).a(s"$crawlerId: ${link.link}").reset())

//    Crawl the webpage and look for <a> elements. Generate
//    pairs of (source, target) tuples and output to a json file.
//    Add new links to crawl by:
//    workQueue.put(WebLink("http://facebook.com"))

    val html =
      """
        |<!DOCTYPE HTML>
        |<html>
        |<head>
        | <title>TechCrunch</title>
        |</head>
        |<body>
        | <a href="http://google.com">Google</a>
        | <div>
        |   <a href="http://yahoo.com">Yahoo</a>
        | </div>
        |</body>
        |</html>
      """.stripMargin

    val htmlElem = try {
      Some(XML.loadString(html))
    } catch {
      case e: Exception => None
    }

    htmlElem match {
      case Some(h) => {
        val newLinks = getLinks(h)

        newLinks.foreach(l => workQueue.put(WebLink(l)))
      }
      case _ => println(ansi().fg(RED).a(s"$crawlerId: ${link.link} cannot be parsed...").reset())
    }
  }

  private def getLinks(html: Elem): Seq[String] = {
    (html \\ "a").map(_ \@ "href")
  }

}
