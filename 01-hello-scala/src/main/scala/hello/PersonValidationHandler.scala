package hello

import com.amazonaws.services.lambda.runtime.Context

object PersonValidationHandler extends LambdaRequestHandler[Person] {
  override def handle(request: Request, ctx: Context) = {
    request.bodyAs[Person]
      .validation
  }
}