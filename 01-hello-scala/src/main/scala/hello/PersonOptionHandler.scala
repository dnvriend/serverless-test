package hello

import com.amazonaws.services.lambda.runtime.Context
import com.github.dnvriend.aws.lambda.handler.{ LambdaRequestHandler, Request, Response }

object PersonOptionHandler extends LambdaRequestHandler[Person] {
  override def handle(request: Request, ctx: Context) = {
    request.bodyOpt[Person]
  }
}
