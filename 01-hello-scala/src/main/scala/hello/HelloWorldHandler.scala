package hello

import com.amazonaws.services.lambda.runtime.Context
import com.github.dnvriend.aws.lambda.handler.{ LambdaRequestHandler, Request, Response }
import play.api.libs.json.{ Format, Json }

object HelloMessage {
  implicit val format: Format[HelloMessage] = Json.format[HelloMessage]
}
case class HelloMessage(message: String)

object HelloWorldHandler extends LambdaRequestHandler {
  override def handle(request: Request, ctx: Context): Response = {
    HelloMessage("Hello World!")
  }
}