name := "serverless-test"

organization := "com.github.dnvriend"

scalaVersion := "2.12.4"

lazy val helloScala = (project in file("01-hello-scala"))
    .dependsOn(awsLambdaHandler, ops)

lazy val awsLambdaHandler = (project in file("aws-lambda-handler"))
    .dependsOn(ops)

lazy val helloKms = (project in file("hello-kms-scala"))
    .dependsOn(ops)

lazy val ops = RootProject(uri("git://github.com/dnvriend/dnvriend-ops.git"))

lazy val helloScalaManual = (project in file("06-hello-scala-manual"))
  .dependsOn(ops)