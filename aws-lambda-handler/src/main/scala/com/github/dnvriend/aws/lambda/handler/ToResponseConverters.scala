package com.github.dnvriend.aws.lambda.handler

import play.api.libs.json.{ Json, Writes }

import scala.language.implicitConversions
import scalaz.{ Disjunction, Validation }

trait ToResponseConverters extends CustomWrites {
  implicit def toApiResponse[E: Writes, A: Writes](that: Disjunction[E, A]): Response = {
    that.fold(error => Response(500, Json.toJson(error)), data => Response(200, Json.toJson(data)))
  }

  implicit def toApiResponse[E: Writes, A: Writes](that: Validation[E, A]): Response = {
    that.fold(error => Response(500, Json.toJson(error)), data => Response(200, Json.toJson(data)))
  }

  implicit def toApiResponse[A: Writes](that: A): Response = {
    Response(200, Json.toJson(that))
  }
}