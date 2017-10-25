package com.github.dnvriend.ops

import java.nio.ByteBuffer

import scala.language.implicitConversions

object ByteBufferOps extends ByteBufferOps

trait ByteBufferOps {
  implicit def toByteBufferOps(that: ByteBuffer): ByteBufferOpsImpl = new ByteBufferOpsImpl(that)
}

class ByteBufferOpsImpl(that: ByteBuffer) {
  def toByteArray: Array[Byte] = {
    that.array()
  }
}

