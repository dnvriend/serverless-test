package com.github.dnvriend.aws.lambda.handler.test

import java.io.File

import cats.Show
import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.resolver.Resolvable
import io.circe.{ Encoder, Json => CirceJson }
import net.jcazevedo.moultingyaml._
import net.jcazevedo.moultingyaml.DefaultYamlProtocol._
import play.api.libs.json._

import scala.language.implicitConversions
import scala.sys.process._
import scala.concurrent.duration._

trait ServerlessTest extends CornichonFeature {
  val projectDir: File = (sys.props ++ sys.env).get("SERVERLESS_PROJECT_DIR").map(new File(_)).getOrElse(fail(
    """
      |Environment variable 'SERVERLESS_PROJECT_DIR' not set.
      |add the following to your build.sbt:
      |
      |envVars in Test := Map("SERVERLESS_PROJECT_DIR" -> baseDirectory.value.absolutePath)
      |
      |or
      |
      |envVars in ItTest := Map("SERVERLESS_PROJECT_DIR" -> baseDirectory.value.absolutePath)
    """.stripMargin))

  override lazy val requestTimeout: FiniteDuration = 24.hours

  lazy val urls: Option[List[String]] = {
    val methods = List("GET", "POST", "PATCH", "DELETE", "PUT")
    val serviceInfo: String = Process("sls info")
      .lineStream_!
      .dropWhile(_ != "Service Information")
      .toList
      .drop(1)
      .mkString("\n")

    val fields = serviceInfo.parseYaml.asYamlObject.fields
    fields.get("endpoints".toYaml).map(_.convertTo[String])
      .map(_.split(" - "))
      .map(_.toList
        .map(methods.map((field: String) => (str: String) => str.replace(field, "")).reduce(_ andThen _))
        .map(_.trim)
        .filterNot(methods.contains)
        .filterNot(_.isEmpty)
      )
  }

  override lazy val baseUrl: String = {
    val url = urls.get.head
    url.substring(0, url.indexOf(".com") + ".com".length)
  }

  override lazy val executeScenariosInParallel: Boolean = true

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