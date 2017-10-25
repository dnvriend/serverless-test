import sbt.Keys._
import sbt._
import sbtrelease.Version

name := "01-hello-scala"

resolvers += Resolver.sonatypeRepo("public")
scalaVersion := "2.12.3"
releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }
assemblyJarName in assembly := "hello.jar"

libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "1.3.0"
libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.16"
libraryDependencies += "net.jcazevedo" %% "moultingyaml" % "0.4.0" % Test
libraryDependencies += "com.github.agourlay" %% "cornichon" % "0.13.0" % Test
libraryDependencies += "org.typelevel" %% "scalaz-scalatest" % "1.1.2" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3" % Test

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings")

//
// setting up integration test
//
def itFilter(name: String): Boolean = name endsWith "IntegrationTest"
def unitFilter(name: String): Boolean = (name endsWith "Test") && !itFilter(name)
lazy val ItTest = config("it") extend(Test)
configs(ItTest)
inConfig(ItTest)(Defaults.testTasks)
fork in Test := true
parallelExecution in Test := false
testOptions in Test := Seq(Tests.Filter(unitFilter))
envVars in Test := Map("SERVERLESS_PROJECT_DIR" -> baseDirectory.value.absolutePath)
fork in ItTest := true
parallelExecution in ItTest := true
testOptions in ItTest := Seq(Tests.Filter(itFilter))
envVars in ItTest := Map("SERVERLESS_PROJECT_DIR" -> baseDirectory.value.absolutePath)

//
// enable scala code formatting //
//
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

// Scalariform settings
SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DanglingCloseParenthesis, Preserve)

//
// enable scalafix linting
//
// enable code rewrite and linting //
scalacOptions ++= scalafixScalacOptions.value // add this line
scalafixVerbose := false

val lintAndRewrite = taskKey[Unit]("Lints and rewrites Scala code using defined rules")

lintAndRewrite := {
  // see: https://scalacenter.github.io/scalafix/docs/users/rules
  List(
    "RemoveUnusedImports", // https://scalacenter.github.io/scalafix/docs/rules/RemoveUnusedImports
    "ExplicitResultTypes", // https://scalacenter.github.io/scalafix/docs/rules/ExplicitResultTypes
    "ProcedureSyntax", // https://scalacenter.github.io/scalafix/docs/rules/ProcedureSyntax
    "DottyVolatileLazyVal", // https://scalacenter.github.io/scalafix/docs/rules/DottyVolatileLazyVal
    "ExplicitUnit", // https://scalacenter.github.io/scalafix/docs/rules/ExplicitUnit
    "DottyVarArgPattern", // https://scalacenter.github.io/scalafix/docs/rules/DottyVarArgPattern
    "NoAutoTupling", // https://scalacenter.github.io/scalafix/docs/rules/NoAutoTupling
    "NoValInForComprehension", // https://scalacenter.github.io/scalafix/docs/rules/NoValInForComprehension
    "NoInfer", // https://scalacenter.github.io/scalafix/docs/rules/NoInfer
  ).map(rule => s" $rule")
    .map(rule => scalafix.toTask(rule))
    .reduce(_ dependsOn _).value
}


val slsDeploy = taskKey[Unit]("Deploy to serverless")
slsDeploy := {
  import scala.sys.process._
  val log = streams.value.log
    log.info("Deploying project")
    (assembly dependsOn clean).value
    Process("sls deploy", baseDirectory.value) ! log
}

val slsDeployLambda = inputKey[Unit]("Deploy a single lamda")
slsDeployLambda := {
  import scala.sys.process._
  import sbt.complete.DefaultParsers._
  val log = streams.value.log
    val lambdaName: String = (Space ~> StringBasic).parsed
    log.info(s"Deploying lambda: $lambdaName")
    (assembly dependsOn clean).value
    Process(s"sls deploy -f $lambdaName", baseDirectory.value) ! log
}

val slsInfo = taskKey[Unit]("Get info about the project")
slsInfo := {
  import scala.sys.process._
  val log = streams.value.log
  log.info("Getting project info")
  Process("sls info", baseDirectory.value) ! log
}

val slsRemove = taskKey[Unit]("Remove the project from AWS")
slsRemove := {
  import scala.sys.process._
  val log = streams.value.log
  log.info("Removing project")
  Process("sls remove", baseDirectory.value) ! log
}

val integrationTest = taskKey[Unit]("Deploys, test an removes the project")
integrationTest := {
  val log = streams.value.log
  log.info("Running full integration test")
  (slsRemove dependsOn (test in ItTest) dependsOn slsDeploy).value
}
