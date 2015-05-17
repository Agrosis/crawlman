package com.appdation.crawlman

import java.util.concurrent._
import com.appdation.crawlman.services.{CrawlerServices, DatabaseService}
import com.plasmaconduit.jsonconfig.JsonConfig
import com.plasmaconduit.validation.{Failure, Success}
import org.fusesource.jansi.AnsiConsole
import org.fusesource.jansi.Ansi._
import org.fusesource.jansi.Ansi.Color._

case class WebCrawler(workers: Int, maxLinks: Int, crawlerServices: CrawlerServices, initialWork: List[WebLink]) {

  private val crawlerPool = Executors.newFixedThreadPool(workers)
  private val workQueue = new LinkedBlockingQueue[WebLink](maxLinks)

  def start(): Unit = {
    initialWork.foreach(w => workQueue.put(w))

    val startLatch = new CountDownLatch(1)
    val crawledLinks = new ConcurrentHashMap[String, String]()

    (1 to workers).foreach(i => {
      crawlerPool.execute(new Crawler(i, crawlerServices, workQueue, crawledLinks, startLatch))
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

    val res = for (
      config <- JsonConfig.loadFromFile("./conf/crawlman.json");
      driver <- config.getString("db.driver");
      url <- config.getString("db.url");
      username <- config.getString("db.user");
      password <- config.getString("db.password");
      initialLink <- config.getString("crawler.initial")
    ) yield {
      val db = DatabaseService(driver, url, username, password)
      db.migrate()

      val crawler = new WebCrawler(4, 200, CrawlerServices(db), List(WebLink(initialLink)))
      crawler.start()
      crawler.shutdown()
    }

    res match {
      case Failure(_) => println(ansi().fg(RED).a(s"Unable to start web crawler. Error in crawler configuration.").reset())
      case _ => {}
    }
  }

}
