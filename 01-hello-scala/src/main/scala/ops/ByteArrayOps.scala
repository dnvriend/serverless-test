package ops

import java.io.{ ByteArrayInputStream, InputStream }
import java.nio.ByteBuffer

import scala.language.implicitConversions
import scala.util.Try

object ByteArrayOps extends ByteArrayOps

trait ByteArrayOps {
  implicit def toByteArrayOps(that: Array[Byte]): ByteArrayOpsImpl = new ByteArrayOpsImpl(that)
}

class ByteArrayOpsImpl(that: Array[Byte]) {
  def hex: String = {
    javax.xml.bind.DatatypeConverter.printHexBinary(that)
  }
  def compress: Array[Byte] = {
    val bos = new java.io.ByteArrayOutputStream(that.length)
    val gzip = new java.util.zip.GZIPOutputStream(bos)
    gzip.write(that)
    gzip.close()
    bos.close()
    bos.toByteArray
  }

  def decompress: Array[Byte] = {
    val bis = new ByteArrayInputStream(that)
    val gzip = new java.util.zip.GZIPInputStream(bis, that.length)
    Stream.continually(gzip.read()).takeWhile(_ != -1).map(_.toByte).toArray
  }

  def toInputStream: InputStream = {
    new java.io.ByteArrayInputStream(that)
  }

  def toByteBuffer: ByteBuffer = {
    java.nio.ByteBuffer.wrap(that)
  }

  def toUtf8String: String = {
    new String(that, "UTF-8")
  }

  def base64: String = {
    java.util.Base64.getEncoder.encodeToString(that)
  }

  def md5: String = {
    import ByteArrayOps._
    java.security.MessageDigest.getInstance("MD5").digest(that).hex
  }

  def sha1: String = {
    import ByteArrayOps._
    java.security.MessageDigest.getInstance("SHA-1").digest(that).hex
  }

  def sha256: String = {
    import ByteArrayOps._
    java.security.MessageDigest.getInstance("SHA-256").digest(that).hex
  }
}