package hello

import com.github.agourlay.cornichon.CornichonFeature
import ops.AllOps
import org.scalatest.{ FlatSpec, Matchers }
import net.jcazevedo.moultingyaml._
import net.jcazevedo.moultingyaml.DefaultYamlProtocol._

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
        .map(_.replace(" GET", ""))
        .map(_.replace(" POST", ""))
        .map(_.trim)
        .filterNot(_ == "GET")
        .filterNot(_ == "POST")
      )
  }

  def getEndpoint(endpoint: String): Option[String] = urls.flatMap(_.find(_.endsWith(endpoint)))
}
