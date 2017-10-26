package com.github.dnvriend.aws.lambda.handler

import java.io.{ InputStream, OutputStream }

import com.amazonaws.services.lambda.runtime.{ Context, RequestStreamHandler }
import com.github.dnvriend.ops.{ AllOps, JsonOps }
import com.github.dnvriend.ops.Functional.DisjunctionNel
import play.api.libs.json._

import scala.language.implicitConversions
import scalaz._
import scalaz.Scalaz._

object Response {
  implicit val format: Format[Response] = Json.format[Response]
  val Ok: Response = Response(200, JsNull, Map("Content-Type" -> "application/json"))
  val Created: Response = Ok.copy(statusCode = 201)
  val Accepted: Response = Ok.copy(statusCode = 202)
  val NoContent: Response = Ok.copy(statusCode = 204)
  val MovedPermanently: Response = Ok.copy(statusCode = 301)
  val SeeOther: Response = Ok.copy(statusCode = 303)
  val TemporaryRedirect: Response = Ok.copy(statusCode = 307)
  val BadRequest: Response = Ok.copy(statusCode = 400)
  val NotFound: Response = Ok.copy(statusCode = 404)
  val InternalServerError: Response = Ok.copy(statusCode = 500)
  val NotImplemented: Response = Ok.copy(statusCode = 501)
  val ServiceUnavailable: Response = Ok.copy(statusCode = 503)
}
case class Response(statusCode: Int, body: JsValue, headers: Map[String, String]) {
  def withBody(data: JsValue): Response = copy(body = data)
  def withHeader(header: (String, String)): Response = copy(headers = this.headers + header)
}

case class Request(request: JsValue) extends AllOps {
  def body: JsValue = request("body")
  def pathParameters: JsValue = request("pathParameters")
  def requestParameters: JsValue = request("queryStringParameters")
  def bodyOpt[A: Reads]: Option[A] = bodyAs[A].toOption

  def bodyAs[A: Reads](implicit validator: Validator[A] = null): DisjunctionNel[Throwable, A] = for {
    data <- Json.parse(body.as[String]).as[A].safeNel
    validated <- (validator.? | Validator.emptyValidator(data)).validate(data).disjunction
  } yield validated

  def bodyAsStringOpt: Option[String] = body.as[String].safe.toOption
  def bodyAsString: Disjunction[Throwable, String] = body.as[String].safe
  def pathParamOpt[A: Reads]: Option[A] = pathParamAs.toOption
  def pathParamAs[A: Reads]: Disjunction[Throwable, A] = pathParameters.as[A].safe
  def requestParamOpt[A: Reads]: Option[A] = requestParamAs.toOption
  def requestParamAs[A: Reads]: Disjunction[Throwable, A] = requestParameters.as[A].safe
}

trait LambdaRequestHandler extends RequestStreamHandler with ToResponseConverters with JsonOps {

  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val request: Request = Request(Json.parse(input))
    val response: Response = handle(request, context)
    val jsBody: JsValue = response.body.escapedJson
    val jsResponse: JsValue = Json.toJson(response.copy(body = jsBody))
    val bytes: Array[Byte] = Json.toBytes(jsResponse)
    output.write(bytes)
    output.close()
  }

  def handle(request: Request, ctx: Context): Response
}