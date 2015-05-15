package com.appdation.crawlman

import java.util.concurrent.{LinkedBlockingQueue, Executors}

import org.fusesource.jansi.AnsiConsole

case class WebCrawler(workers: Int, maxLinks: Int, initialWork: List[WebLink]) {

  private val exec = Executors.newFixedThreadPool(workers)
  private val workQueue = new LinkedBlockingQueue[WebLink](maxLinks)

  def start(): Unit = {
    initialWork.foreach(w => workQueue.put(w))

    (1 to workers).foreach(i => {
      exec.execute(new Crawler(i, workQueue))
    })

    exec.shutdown()
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

    val crawler = new WebCrawler(4, 50, List(WebLink("http://techcrunch.com")))
    crawler.start()
  }

}
