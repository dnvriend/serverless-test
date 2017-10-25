package hello

import com.amazonaws.services.lambda.runtime.Context
import scalaz.Scalaz._

object ErrorHandler extends LambdaRequestHandler[HelloMessage] {
  override def handle(request: Request, ctx: Context): Response = {
    Option.empty[String].toSuccessNel("Error Handler")
  }
}