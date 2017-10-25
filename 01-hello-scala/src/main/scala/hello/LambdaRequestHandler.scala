package hello

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

abstract class LambdaRequestHandler[A: Writes] extends RequestStreamHandler with ToResponseConverters {

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

object Validator {
  def emptyValidator[A](that: A): Validator[A] = (data: A) => {
    Disjunction.right[Throwable, A](data).validationNel[Throwable]
  }

  def validateNonEmpty(fieldName: String, value: String): ValidationNel[String, String] = {
    if (value.trim.isEmpty)
      s"Field '$fieldName' is empty".failureNel[String]
    else
      value.successNel[String]
  }

  def validateNonZero(fieldName: String, value: Long): ValidationNel[String, Long] = {
    if (value == 0) s"Field '$fieldName' with value '$value' may not be zero".failureNel[Long] else value.successNel[String]
  }

  def validateNonNegative(fieldName: String, value: Long): ValidationNel[String, Long] = {
    if (value < 0) s"Field '$fieldName' with value '$value' may not be less than zero".failureNel[Long] else value.successNel[String]
  }

  def validateMinMaxInclusive(fieldName: String, value: Long, min: Long, max: Long): ValidationNel[String, Long] = {
    if (value < min || value > max) s"Field '$fieldName' with value '$value' must be between $min inclusive and $max inclusive".failureNel[Long] else value.successNel[String]
  }
}

trait Validator[A] {
  def validate(data: A): ValidationNel[Throwable, A]
}

case class ValidationException(msg: String) extends RuntimeException(msg)