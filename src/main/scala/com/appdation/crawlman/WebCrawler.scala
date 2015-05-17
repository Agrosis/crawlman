package com.appdation.crawlman

import java.util.concurrent.{CountDownLatch, LinkedBlockingQueue, Executors}
import com.appdation.crawlman.services.{CrawlerServices, DatabaseService}
import com.plasmaconduit.jsonconfig.JsonConfig
import com.plasmaconduit.validation.Success
import org.fusesource.jansi.AnsiConsole
import org.fusesource.jansi.Ansi._
import org.fusesource.jansi.Ansi.Color._

case class WebCrawler(workers: Int, maxLinks: Int, crawlerServices: CrawlerServices, initialWork: List[WebLink]) {

  private val crawlerPool = Executors.newFixedThreadPool(workers)
  private val workQueue = new LinkedBlockingQueue[WebLink](maxLinks)

  def start(): Unit = {
    initialWork.foreach(w => workQueue.put(w))

    val startLatch = new CountDownLatch(1)

    (1 to workers).foreach(i => {
      crawlerPool.execute(new Crawler(i, crawlerServices, workQueue, startLatch))
    })

    startLatch.countDown()
  }

  def shutdown(): Unit = {
    crawlerPool.shutdown()
  }

}

object WebCrawler extends App {

  override def main(args: Array[String]): Unit = {
    AnsiConsole.systemInstall()
    Runtime.getRuntime.addShutdownHook(new Thread(){
      override def run(): Unit = {
        AnsiConsole.systemUninstall()
      }
    })

    DatabaseService.create() match {
      case Success(db) => {
        db.migrate()

        val crawler = new WebCrawler(8, 300, CrawlerServices(db), List(WebLink("http://news.ycombinator.com")))
        crawler.start()
        crawler.shutdown()
      }
      case _ => println(ansi().fg(RED).a(s"Unable to start web crawler because there was an error in database configuration.").reset())
    }
  }

}
