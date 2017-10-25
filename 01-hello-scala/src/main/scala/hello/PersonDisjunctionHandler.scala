package hello

import com.amazonaws.services.lambda.runtime.Context

object PersonDisjunctionHandler extends LambdaRequestHandler[Person] {
  override def handle(request: Request, ctx: Context) = {
    request.bodyAs[Person]
  }
}
