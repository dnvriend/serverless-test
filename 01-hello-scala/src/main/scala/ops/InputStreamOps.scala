package ops

import java.io.InputStream

import play.api.libs.json.{ Json, Reads }
import scala.language.implicitConversions

import scalaz.Disjunction

trait InputStreamOps {
  implicit def toInputStreamOpsImpl(that: InputStream) = new InputStreamOpsImpl(that)
}

class InputStreamOpsImpl(that: InputStream) {
  def toByteArray: Array[Byte] = {
    Stream.continually(that.read()).takeWhile(_ != -1).map(_.toByte).toArray
  }

  def as[A: Reads]: Disjunction[Throwable, A] = {
    Disjunction.fromTryCatchNonFatal(Json.parse(that).as[A])
  }
}
