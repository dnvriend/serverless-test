package hello

import com.amazonaws.services.lambda.runtime.Context
import com.github.dnvriend.aws.lambda.handler.{ LambdaRequestHandler, Request, Response }
import scalaz.Scalaz._

object ErrorHandler extends LambdaRequestHandler {
  override def handle(request: Request, ctx: Context): Response = {
    Option.empty[String].toSuccessNel("Error Handler")
  }
}