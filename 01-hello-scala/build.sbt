import sbt.Keys._
import sbt._
import sbtrelease.Version

name := "hello"

resolvers += Resolver.sonatypeRepo("public")
scalaVersion := "2.12.3"
releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }
assemblyJarName in assembly := "hello.jar"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-jackson" % "3.5.3",
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings")

val slsDeploy = taskKey[Unit]("Deploy to serverless")
slsDeploy := {
    val log = streams.value.log
    val x = (assembly dependsOn clean).value
    "sls deploy".!(log)
}

val slsDeployLambda = inputKey[Unit]("Deploy a single lamda")
slsDeployLambda := {
    import sbt.complete.DefaultParsers._
    val log = streams.value.log
    val lambdaName: String = (Space ~> StringBasic).parsed
    val x = (assembly dependsOn clean).value
    s"sls deploy -f ${lambdaName}".!(log)
}