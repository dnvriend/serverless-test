package com.github.dnvriend.ops

import java.io.InputStream

import com.github.dnvriend.aws.lambda.handler.Validator
import play.api.libs.json._
import play.api.libs.json.{ JsNull, Json }

import scala.language.implicitConversions
import scalaz._
import scalaz.Scalaz._

trait ApiGatewayOps {
  implicit def toApiGatewayOutputStream[A: Writes](body: Option[A]) = new ApiGatewayOpsImpl(body)
  implicit def toApiGatewayJsValueOps(js: JsValue) = new ApiGatewayJsValueOpsImpl(js)
}

object ApiGatewayOpsImpl {
  def gatewayRequest[A: Writes](body: Option[A], pathParameters: Map[String, String] = Map.empty[String, String], path: String = ""): String = {
    val pathParams: String = {
      if (pathParameters.isEmpty) Json.prettyPrint(JsNull)
      else Json.toJson(pathParameters).toString()
    }

    val payload = Json.stringify(JsString(Json.stringify(Json.toJson(body))))

    val apiGatewayRequest =
      s"""
      | {
      |       "resource": "$path",
      |       "path": "$path",
      |       "pathParameters": $pathParams,
      |       "requestParameters": null,
      |       "body": $payload
      |   }
    """.stripMargin

    apiGatewayRequest
  }
}

class ApiGatewayJsValueOpsImpl(response: JsValue) {
  def body: JsValue = response("body")
  def bodyOpt[A: Reads]: Option[A] = {
    bodyAs[A].toOption
  }
  def bodyAs[A: Reads](implicit validator: Validator[A] = null): Disjunction[NonEmptyList[Throwable], A] = for {
    data <- Disjunction.fromTryCatchNonFatal(Json.parse(body.as[String]).as[A]).leftMap(_.wrapNel)
    validated <- Option(validator).getOrElse(Validator.emptyValidator(data)).validate(data).disjunction
  } yield validated

  def bodyAsString: String = body.as[String]
  def statusCode: Int = response("statusCode").as[Int]
  def headers: Map[String, String] = response("headers").as[Map[String, String]]
}

class ApiGatewayOpsImpl[A: Writes](body: Option[A]) extends StringOps {
  import ApiGatewayOpsImpl._
  def toApiGatewayRequestInputStream(pathParams: Map[String, String] = Map.empty[String, String]): InputStream = {
    gatewayRequest(body, pathParams).toInputStream
  }
}
