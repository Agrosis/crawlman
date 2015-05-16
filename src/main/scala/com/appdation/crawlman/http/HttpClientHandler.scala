package com.appdation.crawlman.http

import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}
import io.netty.handler.codec.http.{FullHttpResponse, HttpObject}

final case class HttpClientHandler() extends SimpleChannelInboundHandler[HttpObject] {

  override def channelRead0(ctx: ChannelHandlerContext, msg: HttpObject): Unit = msg match {
    case r: FullHttpResponse => {

    }
    case _ =>
  }

}
