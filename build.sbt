name := "serverless-test"

organization := "com.github.dnvriend"

scalaVersion := "2.12.3"

lazy val helloScala = (project in file("01-hello-scala"))
    .dependsOn(awsLambdaHandler)

lazy val awsLambdaHandler = (project in file("aws-lambda-handler"))