package com.github.dnvriend

import java.io.File

import com.github.dnvriend.ops.AllOps
import play.api.libs.json.Json
import play.api.libs.json._

object SAMTemplate extends AllOps {
  def descriptor(codeUri: String, description: String, lambda: Class[_], path: String, method: String): JsValue = {
    def fqcn(cl: Class[_]): String = cl.getName.replace("$", "")

    def lambdaName(cl: Class[_]): String = cl.getSimpleName.replace("$", "")

    Json.obj(
      "AWSTemplateFormatVersion" -> "2010-09-09",
      "Transform" -> "AWS::Serverless-2016-10-31",
      "Description" -> description,
      "Resources" -> (
        apiResource("dev") ++
        lambdaResource(codeUri, lambdaName(lambda), fqcn(lambda), path, method)
        ),
    ) ++ outputs
  }

  def lambdaResource(codeUri: String, lambdaName: String, fqcn: String, path: String, method: String): JsObject = {
    Json.obj(
      lambdaName -> Json.obj(
        "Type" -> "AWS::Serverless::Function",
        "Properties" -> Json.obj(
          "CodeUri" -> codeUri,
          "Handler" -> s"$fqcn::handleRequest",
          "Events" -> apiEvent(s"${lambdaName}Api", path, method),
          "Runtime" -> "java8",
          "Policies" -> Json.arr("AWSLambdaBasicExecutionRole"),
        )
      )
    )
  }

  /**
    * Creates a collection of Amazon API Gateway resources and methods that can be invoked through HTTPS endpoints.
    *
    * An AWS::Serverless::Api resource need not be explicitly added to a AWS Serverless Application Definition template. A resource of this type is implicitly created from the union of Api events defined on AWS::Serverless::Function resources defined in the template that do not refer to an AWS::Serverless::Api resource. An AWS::Serverless::Api resource should be used to define and document the API using Swagger, which provides more ability to configure the underlying Amazon API Gateway resources.
    */
  def apiResource(stageName: String): JsObject = {
    Json.obj(
      "ApiGatewayApi" -> Json.obj(
        "Type" -> "AWS::Serverless::Api",
        "Properties" -> Json.obj(
          "Name" -> "ApiGatewayApi",
          "DefinitionUri" -> "s3://dnvriend-lambda-helloworld/swagger.json",
          "StageName" -> stageName,
          "CacheClusterEnabled" -> false,
          "CacheClusterSize" -> "0",
        )
      )
    )
  }

  def apiEvent(eventName: String, path: String, method: String): JsObject = {
    Json.obj(
      eventName -> Json.obj(
        "Type" -> "Api",
        "Properties" -> Json.obj(
          "Path" -> path,
          "Method" -> method,
        )
      )
    )
  }

  def outputs(): JsObject = {
    Json.obj(
      "Outputs" -> outputServiceEndpoint()
    )
  }

  def outputServiceEndpoint(): JsValue = {
    Json.obj(
      "ServiceEndpoint" -> Json.obj(
        "Description" -> "URL of the service endpoint",
        "Value" -> Json.obj(
          "Fn::Join" -> Json.arr(
            "",
            Json.arr("https://",
              Json.obj("Ref" -> "ServerlessRestApi"),
              ".execute-api.eu-central-1.amazonaws.com/dev"
            )
          )
        )
      )
    )
  }

  def main(args: Array[String]): Unit = {
    val sam = descriptor(
      "s3://dnvriend-lambda-helloworld/06-hello-scala-manual-0.1-SNAPSHOT.zip",
      "This is an Hello World",
      HelloLambda.getClass,
      "/hello",
      "GET"
    )

    val samJson: String = Json.prettyPrint(sam)
    println(samJson)
    new File("/tmp/sam-template.json").write(samJson)
  }
}
