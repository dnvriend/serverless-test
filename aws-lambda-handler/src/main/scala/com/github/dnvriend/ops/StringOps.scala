package com.github.dnvriend.ops

import java.io.{ ByteArrayInputStream, InputStream }

import scala.language.implicitConversions
import scala.util.matching.Regex

object StringOps extends StringOps

trait StringOps {
  implicit def toStringOps(that: String): StringOpsImpl = new StringOpsImpl(that)
}

class StringOpsImpl(that: String) {
  def fromBase64: Array[Byte] = {
    java.util.Base64.getDecoder.decode(that)
  }

  def fromHex: Array[Byte] = {
    javax.xml.bind.DatatypeConverter.parseHexBinary(that)
  }

  def toInputStream: InputStream = {
    new ByteArrayInputStream(that.getBytes)
  }

  def toUtf8Array: Array[Byte] = {
    that.getBytes("UTF-8")
  }

  def log: String = {
    println(that)
    that
  }

  def find(regex: Regex): Option[String] = {
    regex.findFirstIn(that)
  }

  def findAll(regex: Regex): List[String] = {
    regex.findAllIn(that).toList
  }
}