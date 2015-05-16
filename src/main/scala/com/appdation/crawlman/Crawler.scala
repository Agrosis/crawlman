package com.appdation.crawlman

import java.util.concurrent.{CountDownLatch, BlockingQueue}
import org.fusesource.jansi.Ansi._
import org.fusesource.jansi.Ansi.Color._

import scala.xml.{Elem, XML}

final case class Crawler(crawlerId: Int, workQueue: BlockingQueue[WebLink], latch: CountDownLatch) extends Runnable {

  override def run(): Unit = {
    println(ansi().fg(YELLOW).a(s"$crawlerId: Starting worker thread...").reset())

    latch.await()

    while(true){
      processLink(workQueue.take())
    }
  }

  private def processLink(link: WebLink): Unit = {
    println(ansi().fg(GREEN).a(s"$crawlerId: ${link.link}").reset())

    val links = CrawlerBrowser(link.link).getLinks()//.union(cache)

    links.foreach(s => println(ansi().fg(YELLOW).a(s"$crawlerId: NEW LINK $s")))
    links.foreach(l => workQueue.offer(WebLink(l)))
  }

}
