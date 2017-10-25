package hello

import com.amazonaws.services.lambda.runtime.Context

object PersonOptionHandler extends LambdaRequestHandler[Person] {
  override def handle(request: Request, ctx: Context) = {
    request.bodyOpt[Person]
  }
}
