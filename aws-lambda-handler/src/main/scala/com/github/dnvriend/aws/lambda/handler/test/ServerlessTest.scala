package com.github.dnvriend.aws.lambda.handler.test

import com.github.agourlay.cornichon.CornichonFeature

import scala.concurrent.duration._
import scala.language.implicitConversions

trait ServerlessTest extends CornichonFeature with CornichonCircePlayBridge {
  override lazy val requestTimeout: FiniteDuration = 24.hours

  override lazy val baseUrl: String = {
    val allProps = sys.props ++ sys.env
    allProps.getOrElse(ServerlessTest.ServerlessProjectBaseUrl, fail(ServerlessTest.errorMessage(ServerlessTest.ServerlessProjectBaseUrl)))
  }
  override lazy val executeScenariosInParallel: Boolean = true
}

object ServerlessTest {
  final val ServerlessProjectBaseUrl = "SERVERLESS_PROJECT_BASE_URL"
  def errorMessage(key: String): String = {
    s"""
      |Environment variable '$key' not set.
      |add the following to your build.sbt:
      |
      |envVars in Test := Map("$key" -> "string-value-here")
      |
      |or
      |
      |envVars in ItTest := Map("$key" -> "string-value-here)
    """.stripMargin
  }
}