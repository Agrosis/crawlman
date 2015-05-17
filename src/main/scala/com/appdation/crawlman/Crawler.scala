package com.appdation.crawlman

import java.util.concurrent.{CountDownLatch, BlockingQueue}
import com.appdation.crawlman.services.CrawlerServices
import org.fusesource.jansi.Ansi._
import org.fusesource.jansi.Ansi.Color._

final case class Crawler(crawlerId: Int, crawlerServices: CrawlerServices, workQueue: BlockingQueue[WebLink], latch: CountDownLatch) extends Runnable {

  override def run(): Unit = {
    println(ansi().fg(YELLOW).a(s"$crawlerId: Starting worker thread...").reset())

    latch.await()

    while(true){
      processLink(workQueue.take())
    }
  }

  private def processLink(link: WebLink): Unit = if(!crawlerServices.pageExists(link.link)) {
    println(ansi().fg(GREEN).a(s"$crawlerId: ${link.link}").reset())

    val browser = CrawlerBrowser(link.link)

    for (
      title <- browser.title;
      digest <- browser.htmlDigest
    ) yield {
      val id = crawlerServices.addPage(link.link, title, digest)

      val links = browser.links().filter(l => !crawlerServices.pageExists(l))
      links.foreach(l => {
        if(workQueue.offer(WebLink(l))){
//          println(ansi().fg(YELLOW).a(s"$crawlerId: NEW LINK $l"))
          crawlerServices.addLink(id, l)
        }
      })
    }
  }

}
