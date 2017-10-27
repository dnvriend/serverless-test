package com.github.dnvriend.ops

import java.io.OutputStream

import play.api.libs.json.{ JsValue, Json, Writes }

trait JsonOps {
  implicit def ToJsonOpsImpl(that: JsValue): JsonOpsImpl = new JsonOpsImpl(that)
  implicit def ToOutputStreamOps[A <: Product: Writes](that: A) = new JsonToOutputStreamOps(that)
}

class JsonOpsImpl(that: JsValue) {
  def log: JsValue = {
    println(that.toString())
    that
  }

  def escapedJson: JsValue = {
    Json.toJson(that.toString)
  }

  def str: String = {
    that.toString()
  }

  def pretty: String = {
    Json.prettyPrint(that)
  }

  def bytes: Array[Byte] = {
    that.toString.getBytes("UTF-8")
  }
}

class JsonToOutputStreamOps[A <: Product: Writes](that: A) extends StringOps {
  def write(os: OutputStream): Unit = {
    os.write(Json.toJson(that).toString().toUtf8Array)
    os.close()
  }
}
