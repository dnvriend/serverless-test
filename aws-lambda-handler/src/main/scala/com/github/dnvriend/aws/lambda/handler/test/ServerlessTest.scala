package com.github.dnvriend.aws.lambda.handler.test

import java.io.File

import com.github.agourlay.cornichon.CornichonFeature
import net.jcazevedo.moultingyaml.DefaultYamlProtocol._
import net.jcazevedo.moultingyaml._

import scala.concurrent.duration._
import scala.language.implicitConversions
import scala.sys.process._

trait ServerlessTest extends CornichonFeature with CornichonCircePlayBridge {
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
}