package hello

import java.io.{ InputStream, OutputStream }

import com.amazonaws.services.lambda.runtime.Context
import hello.mock.MockContext
import ops.AllOps
import org.scalatest.{ FlatSpec, Matchers, OptionValues, TryValues }
import org.typelevel.scalatest.{ DisjunctionMatchers, DisjunctionValues, ValidationMatchers, ValidationValues }
import play.api.libs.json._

import scala.language.implicitConversions

abstract class TestSpec extends FlatSpec with Matchers with DisjunctionMatchers with DisjunctionValues with ValidationMatchers with ValidationValues with OptionValues with TryValues with AllOps {
  def withApiGatewayRequest[A: Writes](a: Option[A] = Option.empty[A], pathParams: Map[String, String] = Map.empty[String, String])(f: InputStream => OutputStream => Context => Unit): Array[Byte] = {
    withOutputStream { os =>
      f(a.toApiGatewayRequestInputStream(pathParams))(os)(MockContext)
    }
  }
}
