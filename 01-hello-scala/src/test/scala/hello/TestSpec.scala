package hello

import cats.Show
import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.resolver.Resolvable
import io.circe.{ Encoder, Json => CirceJson }
import ops.AllOps
import org.scalatest.{ FlatSpec, Matchers }
import net.jcazevedo.moultingyaml._
import net.jcazevedo.moultingyaml.DefaultYamlProtocol._
import play.api.libs.json._

import scala.language.implicitConversions
import scala.sys.process._

abstract class TestSpec extends FlatSpec with Matchers with AllOps {

}

trait ServerlessTest extends CornichonFeature {
  import scala.concurrent.duration._
  override lazy val requestTimeout = 10.seconds
  lazy val urls: Option[List[String]] = {
    val serviceInfo: String = "sls info".lineStream_!.dropWhile(_ != "Service Information").toList.drop(1).mkString("\n")
    val fields = serviceInfo.parseYaml.asYamlObject.fields
    fields.get("endpoints".toYaml).map(_.convertTo[String])
      .map(_.split(" - "))
      .map(_.toList
        .map(_.trim)
        .map(_.replace("GET", ""))
        .map(_.replace("POST", ""))
        .map(_.replace("PATCH", ""))
        .map(_.replace("DELETE", ""))
        .map(_.replace("PUT", ""))
        .map(_.trim)
        .filterNot(_ == "GET")
        .filterNot(_ == "POST")
        .filterNot(_ == "PATCH")
        .filterNot(_ == "DELETE")
        .filterNot(_ == "PUT")
        .filterNot(_.isEmpty)
      )
  }

  override lazy val baseUrl = {
    val url = urls.get.head
    url.substring(0, url.indexOf(".com") + ".com".length)
  }

  def getEndpoint(endpoint: String): Option[String] = urls.flatMap(_.find(_.endsWith(endpoint)))

  implicit def jsonResolvableForm[A <: Product: Format]: Resolvable[A] = new Resolvable[A] {
    def toResolvableForm(data: A): String = showJson.show(data)
    def fromResolvableForm(s: String): A = Json.parse(s).as[A]
  }

  implicit def showJson[A <: Product: Writes]: Show[A] = (data: A) => {
    Json.toJson(data).toString
  }

  implicit def JsonEncoder[A <: Product: Writes]: Encoder[A] = (data: A) => {
    io.circe.parser.parse(Json.toJson(data).toString).getOrElse(CirceJson.Null)
  }
}
