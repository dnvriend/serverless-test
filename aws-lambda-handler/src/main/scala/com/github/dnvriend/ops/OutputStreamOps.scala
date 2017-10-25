package com.github.dnvriend.ops

import java.io.OutputStream

import play.api.libs.json.{ Json, Writes }
import scala.language.implicitConversions

trait OutputStreamOps {
  def toOutputStreamOps[A <: Product: Writes](that: A) = new OutputStreamOpsImpl(that)
}

class OutputStreamOpsImpl[A <: Product: Writes](that: A) extends StringOps {
  def write(os: OutputStream): Unit = {
    os.write(Json.toJson(that).toString().toUtf8Array)
    os.close()
  }
}
