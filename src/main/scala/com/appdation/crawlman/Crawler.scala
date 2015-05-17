package com.appdation.crawlman

import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.{ConcurrentHashMap, CountDownLatch, BlockingQueue}
import com.appdation.crawlman.services.CrawlerServices
import com.plasmaconduit.url.URL
import org.fusesource.jansi.Ansi._
import org.fusesource.jansi.Ansi.Color._

import scala.slick.driver.MySQLDriver.simple._

final case class Crawler(crawlerId: Int, crawlerServices: CrawlerServices, workQueue: BlockingQueue[WebLink], crawledLinks: ConcurrentHashMap[String, String], latch: CountDownLatch) extends Runnable {

  override def run(): Unit = {
    println(ansi().fg(YELLOW).a(s"$crawlerId: Starting worker thread...").reset())

    latch.await()

    crawlerServices.database.database { implicit session =>
      while (true) {
        processLink(workQueue.take())
      }
    }
  }

  private def processLink(link: WebLink)(implicit session: Session): Unit = if(crawledLinks.put(hash(link.link), link.link) != link.link) {
    println(ansi().fg(GREEN).a(s"$crawlerId: ${link.link}").reset())

    val browser = CrawlerBrowser(link.link)

    for (
      title <- browser.title;
      html <- browser.html;
      url <- URL(link.link).toOption
    ) yield {
      val id = crawlerServices.addPage(link.link, title, hash(html))

      val links = browser.links().filter(l => !crawlerServices.pageExists(l))
      links.foreach(l => {
        if(!workQueue.contains(l) && workQueue.offer(WebLink(l))) {
          crawlerServices.addLink(id, l)
        }
      })
    }
  }

  private def hash(input: String): String = {
    val md = MessageDigest.getInstance("SHA-1")
    md.update(input.getBytes("UTF-8"))
    new BigInteger(1, md.digest()).toString(16)
  }

}
