package com.appdation.crawlman

import java.util.concurrent.BlockingQueue
import org.fusesource.jansi.Ansi._
import org.fusesource.jansi.Ansi.Color._

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
  }

}
