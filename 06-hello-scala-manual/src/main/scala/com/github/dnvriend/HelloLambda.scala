package com.github.dnvriend

import java.io.{ InputStream, OutputStream }

import com.amazonaws.services.lambda.runtime.{ Context, RequestStreamHandler }
import com.github.dnvriend.ops.AllOps

import scala.compat.Platform

object HelloLambda extends RequestStreamHandler with AllOps {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val str: String = input.toByteArray.str
    val logMsg: String = "Received: " + str
    println(logMsg)
    context.getLogger.log(logMsg)
    val response: String = "Hello from Lambda: " + Platform.currentTime
    output.write(response.getBytes("UTF-8"))
    output.close()
  }
}
