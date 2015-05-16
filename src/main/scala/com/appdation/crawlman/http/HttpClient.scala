package com.appdation.crawlman.http

import io.netty.channel.nio.NioEventLoopGroup

// Provides a blocking http client
case class HttpClient() {



}

object HttpClient {

  private val eventLoop = new NioEventLoopGroup()

}
