package com.appdation.crawlman

import java.util.concurrent.{CountDownLatch, LinkedBlockingQueue, Executors}

import org.fusesource.jansi.AnsiConsole

case class WebCrawler(workers: Int, maxLinks: Int, initialWork: List[WebLink]) {

  private val crawlerPool = Executors.newFixedThreadPool(workers)
  private val workQueue = new LinkedBlockingQueue[WebLink](maxLinks)

  def start(): Unit = {
    initialWork.foreach(w => workQueue.put(w))

    val startLatch = new CountDownLatch(1)

    (1 to workers).foreach(i => {
      crawlerPool.execute(new Crawler(i, workQueue, startLatch))
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

    val crawler = new WebCrawler(8, 300, List(WebLink("http://news.ycombinator.com")))
    crawler.start()
    crawler.shutdown()
  }

}
