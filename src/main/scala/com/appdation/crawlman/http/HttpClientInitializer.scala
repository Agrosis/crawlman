package com.appdation.crawlman.http

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.{HttpObjectAggregator, HttpContentDecompressor, HttpClientCodec}
import io.netty.handler.ssl.SslContext

final case class HttpClientInitializer(sslContext: Option[SslContext]) extends ChannelInitializer[SocketChannel] {

  override def initChannel(ch: SocketChannel): Unit = {
    val p = ch.pipeline()

    sslContext.foreach(s => p.addLast(s.newHandler(ch.alloc())))
    p.addLast(new HttpContentDecompressor())
    p.addLast(new HttpObjectAggregator(1048576))
    p.addLast(new HttpClientCodec())

  }

}
