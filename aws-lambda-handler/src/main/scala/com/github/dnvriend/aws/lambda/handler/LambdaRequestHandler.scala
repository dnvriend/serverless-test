package com.github.dnvriend.aws.lambda.handler

import java.io.{ InputStream, OutputStream }

import com.amazonaws.services.lambda.runtime.{ Context, RequestStreamHandler }
import play.api.libs.json._

import scala.language.implicitConversions
import scalaz._
import scalaz.Scalaz._

object Response {
  implicit val format: Format[Response] = Json.format[Response]
}
case class Response(statusCode: Int, body: JsValue, headers: Map[String, String] = Map.empty[String, String])

case class Request(request: JsValue) {
  def body: JsValue = request("body")
  def pathParameters: JsValue = request("pathParameters")
  def requestParameters: JsValue = request("requestParameters")
  def bodyOpt[A: Reads]: Option[A] = {
    bodyAs[A].toOption
  }
  def bodyAs[A: Reads](implicit validator: Validator[A] = null): Disjunction[NonEmptyList[Throwable], A] = for {
    data <- Disjunction.fromTryCatchNonFatal(Json.parse(body.as[String]).as[A]).leftMap(_.wrapNel)
    validated <- Option(validator).getOrElse(Validator.emptyValidator(data)).validate(data).disjunction
  } yield validated

  def bodyAsString: String = body.as[String]
  def pathParamsOpt[A: Reads]: Option[A] = pathParamsAs.toOption
  def pathParamsAs[A: Reads]: Disjunction[Throwable, A] = Disjunction.fromTryCatchNonFatal {
    pathParameters.as[A]
  }
  def requestParamsOpt[A: Reads]: Option[A] = requestParamsAs.toOption
  def requestParamsAs[A: Reads]: Disjunction[Throwable, A] = Disjunction.fromTryCatchNonFatal {
    requestParameters.as[A]
  }
}

abstract class LambdaRequestHandler extends RequestStreamHandler with ToResponseConverters {

  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val request: Request = Request(Json.parse(input))
    val response: Response = handle(request, context)
    val jsBody = Json.toJson(Json.prettyPrint(response.body))
    val jsResponse = Json.toJson(response.copy(body = jsBody))
    val bytes = Json.toBytes(jsResponse)
    output.write(bytes)
    output.close()
  }

  def handle(request: Request, ctx: Context): Response
}