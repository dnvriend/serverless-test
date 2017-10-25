package com.github.dnvriend.aws.lambda.handler

import play.api.libs.json.{ JsResultException, Json, Writes }

import scalaz._
import scalaz.Scalaz._

trait CustomWrites {
  implicit def DisjunctionWrites[E: Writes, A: Writes]: Writes[Disjunction[E, A]] = (d: Disjunction[E, A]) => {
    d.fold(e => Json.toJson[E](e), a => Json.toJson[A](a))
  }

  implicit def ThrowableWrites: Writes[Throwable] = {
    case t: JsResultException => Json.toJson(t)(JsResultExceptionWrites)
    case t: Throwable         => Json.toJson(t.getMessage)
  }

  implicit def NonEmptyListWrites[A: Writes]: Writes[NonEmptyList[A]] = (o: NonEmptyList[A]) => {
    Json.toJson(o.toList)
  }

  implicit def JsResultExceptionWrites: Writes[JsResultException] = (err: JsResultException) => {
    Json.toJson(err.errors.map {
      case (path, errors) => (path.toString, errors.flatMap(_.messages).mkString(","))
    }.toList)
  }
}