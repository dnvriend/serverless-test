package com.github.dnvriend.ops

import play.api.libs.json.{ JsValue, Json }

trait JsonOps {
  implicit def ToJsonOpsImpl(that: JsValue): JsonOpsImpl = new JsonOpsImpl(that)
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
