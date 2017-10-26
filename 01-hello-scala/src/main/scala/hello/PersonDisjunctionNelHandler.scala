package hello

import com.amazonaws.services.lambda.runtime.Context
import com.github.dnvriend.aws.lambda.handler.{ LambdaRequestHandler, Request, Response }

object PersonDisjunctionNelHandler extends LambdaRequestHandler {
  override def handle(request: Request, ctx: Context) = {
    request.bodyAs[Person]
  }
}