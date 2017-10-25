package ops

import com.amazonaws.services.lambda.runtime.Context
import scala.language.implicitConversions

trait ContextOps {
  implicit def toContextOps(that: Context) = new ContextOpsImpl(that)
}

class ContextOpsImpl(that: Context) {
  def log(msg: String): String = {
    that.getLogger.log(msg)
    msg
  }
}