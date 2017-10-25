package com.github.dnvriend.aws.lambda.handler.test

import cats.Show
import com.github.agourlay.cornichon.resolver.Resolvable
import io.circe.{ Encoder, Json => CirceJson }
import play.api.libs.json.{ Format, Json, Writes }

trait CornichonCircePlayBridge {
  implicit def jsonResolvableForm[A <: Product: Format]: Resolvable[A] = new Resolvable[A] {
    def toResolvableForm(data: A): String = showJson.show(data)

    def fromResolvableForm(s: String): A = Json.parse(s).as[A]
  }

  implicit def showJson[A <: Product: Writes]: Show[A] = (data: A) => {
    Json.toJson(data).toString
  }

  implicit def JsonEncoder[A <: Product: Writes]: Encoder[A] = (data: A) => {
    io.circe.parser.parse(Json.toJson(data).toString).getOrElse(CirceJson.Null)
  }
}
