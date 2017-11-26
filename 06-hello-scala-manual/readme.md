# 06-hello-scala-manual

## Introduction
This time, we will create and deploy a lambda by hand. Let's see what we have to do.

## Create the lambda
Create a lambda that uses the following libraries:

```
libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.2.0"
libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "2.0.2"
```

.. and implements the `com.amazonaws.services.lambda.runtime.RequestStreamHandler` interface.

## Package
The lambda must be packaged to a zip file, using the [sbt-native-packager](http://sbt-native-packager.readthedocs.io/en/stable/index.html), but the zip must not have a toplevel directory:

```
topLevelDirectory := None
```

Using the `universal:packageBin` command, the zip is available in the `target/universal/

## Configuration
The [AWS Serverless Application Model (AWS SAM)](https://github.com/awslabs/serverless-application-model) is a model to define serverless applications. [AWS SAM](http://docs.aws.amazon.com/lambda/latest/dg/deploying-lambda-apps.html#serverless_app) is natively supported by [AWS CloudFormation](https://aws.amazon.com/cloudformation/) and provides a simplified way of defining the Amazon API Gateway APIs, AWS Lambda functions, and Amazon DynamoDB tables needed by your serverless application.

AWS SAM supports special resource types that simplify how to express functions, APIs, mappings, and DynamoDB tables for serverless applications, as well as some features for these services like environment variables. The AWS CloudFormation description of these resources conforms to the AWS Serverless Application Model. In order to deploy your application, simply specify the resources you need as part of your application, along with their associated permissions policies in an AWS CloudFormation template file (written in either JSON or YAML), package your deployment artifacts, and deploy the template.

So we need a `sam-template` to describe the lambda we want to deploy and how to which resources to 'wire' it:

```json
{
  "AWSTemplateFormatVersion" : "2010-09-09",
  "Transform" : "AWS::Serverless-2016-10-31",
  "Description" : "This is an Hello World",
  "Resources" : {
    "HelloLambda" : {
      "Type" : "AWS::Serverless::Function",
      "Properties" : {
        "CodeUri" : "s3://dnvriend-lambda-helloworld/06-hello-scala-manual-0.1-SNAPSHOT.zip",
        "Handler" : "com.github.dnvriend.HelloLambda::handleRequest",
        "Events" : {
          "HelloLambdaApi" : {
            "Type" : "Api",
            "Properties" : {
              "Path" : "/hello",
              "Method" : "GET"
            }
          }
        },
        "Runtime" : "java8",
        "Policies" : [ "AWSLambdaBasicExecutionRole" ]
      }
    }
  }
}
```

## Deploy
To deploy a lambda, you have to do two things:

1. Upload the archive to s3
2. Instruct Cloudformation to deploy the lambda using the `sam-template.json` template

## Upload zip to S3
To deploy the lambda, create an s3 bucket to hold the resources for the lambda eg. the class files and libraries and upload the `06-hello-scala-manual-0.1-SNAPSHOT.zip` archive to s3:

```
aws s3 mb s3://dnvriend-lambda-helloworld
aws s3 cp ~/projects/serverless-test/06-hello-scala-manual/target/universal/06-hello-scala-manual-0.1-SNAPSHOT.zip s3://dnvriend-lambda-helloworld
aws s3 cp ~/projects/serverless-test/06-hello-scala-manual/swagger.json s3://dnvriend-lambda-helloworld
```

## Validate the sam-template
To validate the sam template:

```
aws cloudformation validate-template --template-body file://sam-template.json
```

## Deploy using Cloudformation
Now deploy the lambda using Cloudformation:

```
aws cloudformation deploy \
  --template-file sam-template.json \
  --stack-name dev-hello-scala \
  --capabilities CAPABILITY_IAM
```

## Descibing the stack event
The `describe-stack-events`, returns all stack related events for a specified stack in reverse chronological order. This command is handy when the stack fails to create or update, so fetching the list of events leading up to the failure can give insights to what has gone wrong.

```
aws cloudformation describe-stack-events \
  --stack-name dev-hello-scala
```

## Updating a stack
[Stacks cannot be updated when it contains transforms](https://stackoverflow.com/questions/41269337/update-cloudformation-stack-from-aws-cli-with-sam-transform). These stacks must be deployed again instead of updated. The update command is necessary because Transforms need to be applied using change sets, which the `deploy` command automates for you. Refer to [Working with Stacks that Contain Transforms](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/transform-section-structure.html#transform-section-structure-change-set) for further discussion.

```
aws cloudformation deploy \
  --template-file sam-template.json \
  --stack-name dev-hello-scala \
  --capabilities CAPABILITY_IAM
```

## Information about the stack
To get information about the stack:

```
$ aws cloudformation list-stack-resources --stack-name dev-hello-scala
```

The output is:

```
{
    "StackResourceSummaries": [
        {
            "LogicalResourceId": "HelloLambda",
            "PhysicalResourceId": "dev-hello-scala-HelloLambda-CIPB370LC5J",
            "ResourceType": "AWS::Lambda::Function",
            "LastUpdatedTimestamp": "2017-11-25T13:54:19.068Z",
            "ResourceStatus": "CREATE_COMPLETE"
        },
        {
            "LogicalResourceId": "HelloLambdaHelloLambdaApiPermissionProd",
            "PhysicalResourceId": "dev-hello-scala-HelloLambdaHelloLambdaApiPermissionProd-1T51HIPIZC1R9",
            "ResourceType": "AWS::Lambda::Permission",
            "LastUpdatedTimestamp": "2017-11-25T13:54:36.625Z",
            "ResourceStatus": "CREATE_COMPLETE"
        },
        {
            "LogicalResourceId": "HelloLambdaHelloLambdaApiPermissionTest",
            "PhysicalResourceId": "dev-hello-scala-HelloLambdaHelloLambdaApiPermissionTest-Z7AJQCE7VA8O",
            "ResourceType": "AWS::Lambda::Permission",
            "LastUpdatedTimestamp": "2017-11-25T13:54:35.911Z",
            "ResourceStatus": "CREATE_COMPLETE"
        },
        {
            "LogicalResourceId": "HelloLambdaRole",
            "PhysicalResourceId": "dev-hello-scala-HelloLambdaRole-UHQDOC56QXCU",
            "ResourceType": "AWS::IAM::Role",
            "LastUpdatedTimestamp": "2017-11-25T13:54:13.846Z",
            "ResourceStatus": "CREATE_COMPLETE"
        },
        {
            "LogicalResourceId": "ServerlessRestApi",
            "PhysicalResourceId": "esgr20wmlj",
            "ResourceType": "AWS::ApiGateway::RestApi",
            "LastUpdatedTimestamp": "2017-11-25T13:54:22.844Z",
            "ResourceStatus": "CREATE_COMPLETE"
        },
        {
            "LogicalResourceId": "ServerlessRestApiDeployment2a80907732",
            "PhysicalResourceId": "fm8ep0",
            "ResourceType": "AWS::ApiGateway::Deployment",
            "LastUpdatedTimestamp": "2017-11-25T13:54:26.678Z",
            "ResourceStatus": "CREATE_COMPLETE"
        },
        {
            "LogicalResourceId": "ServerlessRestApiProdStage",
            "PhysicalResourceId": "Prod",
            "ResourceType": "AWS::ApiGateway::Stage",
            "LastUpdatedTimestamp": "2017-11-25T13:54:30.739Z",
            "ResourceStatus": "CREATE_COMPLETE"
        }
    ]
}
```


## Invoke the function

## Uninstall

## Resources
- http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model.html
- http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model-handler-types.html
- http://docs.aws.amazon.com/lambda/latest/dg/java-handler-using-predefined-interfaces.html
- http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model-req-resp.html
- http://docs.aws.amazon.com/lambda/latest/dg/access-control-resource-based.html
- http://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-stream.html
- https://aws.amazon.com/about-aws/whats-new/2016/11/introducing-the-aws-serverless-application-model/
- http://yaml-online-parser.appspot.com/