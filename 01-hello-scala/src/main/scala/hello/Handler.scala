package hello

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler, RequestStreamHandler}
import org.json4s.jackson.{JsonMethods, Serialization}
import org.json4s.jackson.Serialization._
import org.json4s.{Formats, NoTypeHints, _}

import scala.language.implicitConversions
import scala.beans.BeanProperty

case class MyRequest(@BeanProperty var key1: String, @BeanProperty var key2: String, @BeanProperty var key3: String) {
  def this() = this("", "", "")
}

case class MyResponse(@BeanProperty message: String, @BeanProperty request: MyRequest)

object Person {
	implicit def fromApiGatewayRequest(req: Request)(implicit formats: Formats): Person = {
		req.bodyAs[Person]
	}
}
case class Person(name: String, age: Int)

case class PersonWithAgeString(name: String, age: String)

object MethodBased {
	implicit val formats: Formats = Serialization.formats(NoTypeHints)

	def myHandlerMethod(input: MyRequest, context: Context): MyResponse = {
		context.getLogger.log("invoked with: " + input)
		MyResponse("Hi, this function worked!", input)
	}

	def streamHandler(input: InputStream, output: OutputStream, ctx: Context): Unit = {
		val req = input: Request
		println(req)
		val person: Person = req
		Serialization.write(Response.OK(Serialization.write(person)), output)
	}

	def inputAsStringHandler(input: InputStream, output: OutputStream, ctx: Context): Unit = {
		val inputAsString: String = io.Source.fromInputStream(input, "UTF-8").mkString
		Serialization.write(Response.OK(inputAsString), output)
	}

	def inputAsApiGatewayRequest(input: InputStream, output: OutputStream, ctx: Context): Unit = {
		val inputAsString: String = io.Source.fromInputStream(input, "UTF-8").mkString
		ctx.getLogger.log(inputAsString)
		val map: Map[String, Any] = JsonMethods.parse(input).extract[Map[String, Any]]
		val request: Request = Request(map)
		ctx.getLogger.log(request.toString)
		Serialization.write(Response.OK(Serialization.write(request.bodyAs[Person])), output)
	}

	implicit class InputStreamUtil(input: InputStream) {
		def asString: String = io.Source.fromInputStream(input, "UTF-8").mkString
		def request: Request = input
	}
}

class RequestHandlerBased extends RequestHandler[MyRequest, MyResponse] {
	override def handleRequest(input: MyRequest, context: Context): MyResponse = {
		context.getLogger.log("invoked with: " + input)
		MyResponse("Hi, this function worked!", input)
	}
}


/**
	* The response to the API Gateway must be compliant to the following message:
	* {
    "isBase64Encoded": true|false,
    "statusCode": httpStatusCode,
    "headers": { "headerName": "headerValue", ... },
    "body": "..."
}
	*/

/**
The API GatewayRequest contains the following:
	{
    "resource": "Resource path",
    "path": "Path parameter",
    "httpMethod": "Incoming request's method name"
    "headers": {Incoming request headers}
    "queryStringParameters": {query string parameters }
    "pathParameters":  {path parameters}
    "stageVariables": {Applicable stage variables}
    "requestContext": {Request context, including authorizer-returned key-value pairs}
    "body": "A JSON string of the request payload."
    "isBase64Encoded": "A boolean flag to indicate if the applicable request payload is Base64-encode"
}
	*/



//object Test extends App {
//	val json =
//  """
//		|{"resource":"/stream",
//|"path":"/stream",
//|"httpMethod":"POST",
//|"headers":{"Accept":"application/json, */*","Accept-Encoding":"gzip, deflate","CloudFront-Forwarded-Proto":"https","CloudFront-Is-Desktop-Viewer":"true","CloudFront-Is-Mobile-Viewer":"false","CloudFront-Is-SmartTV-Viewer":"false","CloudFront-Is-Tablet-Viewer":"false","CloudFront-Viewer-Country":"NL","Content-Type":"application/json","Host":"fxeixg0qni.execute-api.us-east-1.amazonaws.com","User-Agent":"HTTPie/0.9.9","Via":"1.1 d5e8c461ea4d131327b2ba97a2d7f473.cloudfront.net (CloudFront)","X-Amz-Cf-Id":"1djsqANhRhh-eiCMyIUjkfYptgDQlhQbk5v6_JJKkOwfUe9OU7OrSA==","X-Amzn-Trace-Id":"Root=1-59e09d70-1a40a5404161ad445a9a658a","X-Forwarded-For":"89.255.4.114, 54.239.167.84","X-Forwarded-Port":"443","X-Forwarded-Proto":"https"},"queryStringParameters":null,"pathParameters":null,"stageVariables":null,"requestContext":{"path":"/dev/stream","accountId":"436740350302","resourceId":"949z00","stage":"dev","requestId":"1aa89ee2-b006-11e7-a1e6-f506a42d0c92","identity":{"cognitoIdentityPoolId":null,"accountId":null,"cognitoIdentityId":null,"caller":null,"apiKey":"","sourceIp":"89.255.4.114","accessKey":null,"cognitoAuthenticationType":null,"cognitoAuthenticationProvider":null,"userArn":null,"userAgent":"HTTPie/0.9.9","user":null},"resourcePath":"/stream","httpMethod":"POST","apiId":"fxeixg0qni"},"body":"{\"name\": \"dennis\", \"age\": 42}","isBase64Encoded":false}
//  """.stripMargin
//
//
//
//	implicit val formats: Formats = Serialization.formats(NoTypeHints)
//
//	val req: Request = new ByteArrayInputStream(json.getBytes())
//	println(req.bodyAs[Person])
//	val map = JsonMethods.parse(json).extract[Map[String, Any]]
//	val str = map("message").asInstanceOf[String]
//	println(str)
////	val p = read[Map[String, Any]](str)
////	println(read[Person](write(p)))
////	val req = ApiGatewayRequest(map)
////	val p2 = req.bodyAs[Person]
////	println("p2: " + p2)
////		val p: Person = parse()
////	println(req.pathParamAs[PersonWithAgeString])
//}


////////
////
object Request {
	implicit def fromInputStream(input: InputStream)(implicit formats: Formats): Request = {
		val map = JsonMethods.parse(input).extract[Map[String, Any]]
		Request(map)
	}
}
case class Request(request: Map[String, Any]) {
	def bodyAs[A: Manifest](implicit formats: Formats): A = Serialization.read[A](body)
	def pathParamAs[A: Manifest](implicit formats: Formats): A = Serialization.read[A](pathParamAsJson)
	def queryParamsAs[A: Manifest](implicit formats: Formats): A = Serialization.read[A](queryParamsAsJson)
	def body: String = request("body").asInstanceOf[String]
	def requestContext: Map[String, Any] = request.getMap("requestContext")
	def pathParams: Map[String, Any] = request.getMap("pathParameters")
	def pathParamAsJson(implicit formats: Formats): String = pathParams.asJson
	def queryParamsAsJson(implicit formats: Formats): String = request.getMap("queryStringParameters").asJson
	def resource: String = request.yoloAs("resource")
	def path: String = request.yoloAs("path")
	def method: String = request.yoloAs("method")
	def headers: Map[String, Any] = request.yoloAs("headers")

	implicit class MapExtensions(that: Map[String, Any]) {
		def yoloAs[A](key: String): A = that(key).asInstanceOf[A]
		def getMap(key: String): Map[String, Any] = that.yoloAs[Map[String, Any]](key)
		def asJson(implicit formats: Formats): String = Serialization.write(that)
	}
}

object Response {
	def OK[A](that: A)= Response(200, that)
	def NOK[A](that: A) = Response(400, that)
	def NotFound[A](that: A) = Response(404, that)
}
case class Response[A](statusCode: Int, body: A, headers: Map[String, String] = Map.empty[String, String])

trait LambdaRequestHandler[Req <: AnyRef, Resp <: AnyRef] extends RequestStreamHandler {
	implicit lazy val formats: Formats = Serialization.formats(NoTypeHints)
	override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
		val request: Request = input
		val response: Response[Resp] = handle(request, context)
		Serialization.write(Response(statusCode = response.statusCode, body = Serialization.write(response.body), headers = response.headers), output)
	}

	def handle(request: Request, ctx: Context): Response[Resp]
}

case class GetPerson(id: Int)

object MyScalaHandler extends LambdaRequestHandler[GetPerson, Person] {
	override def handle(req: Request, ctx: Context): Response[Person] = {
		val person = req.bodyAs[GetPerson]
		println(person)
		Response.OK(Person("fff", 99))
	}
}

object Test extends App {
	val reqHandler = new LambdaRequestHandler[Nothing, Either[Error, Person]] {
		override def handle(request: Request, ctx: Context): Response[Either[Error, Person]] = {
			val xx: Option[Response[Either[Error, Person]]] = None
				Option(Response.OK(Right(Person("dennis", 42))))
			xx.getOrElse(Response.NOK(Left(Error("bad lama"))))
		}
	}

	val in =
  """
		|{
|    "body": "{\"id\": 42}",
|    "resource": "Resource path",
|    "path": "Path parameter",
|    "httpMethod": "",
|    "headers": {},
|    "queryStringParameters": {
|    		"name": "dennis",
|      	"age": "42"
|    },
|    "pathParameters": {
|    		"name": "dennis",
|      	"age": "42"
|    },
|    "stageVariables": {},
|    "requestContext": {},
|    "body": "",
|    "isBase64Encoded": false
|}
  """.stripMargin.getBytes
	val out = new ByteArrayOutputStream()
	val input = new ByteArrayInputStream(in)
	reqHandler.handleRequest(input, out, null)
	println(new String(out.toByteArray, "UTF-8"))
}

case class Error(msg: String)



