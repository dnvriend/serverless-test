package com.github.dnvriend.aws.lambda.handler.mock

import com.amazonaws.services.lambda.runtime._

object MockContext extends Context {
  override def getFunctionName: String = "MockContext"

  override def getRemainingTimeInMillis: Int = Int.MaxValue

  override def getLogger: LambdaLogger = MockLambdaLogger

  override def getFunctionVersion: String = "MockContext"

  override def getMemoryLimitInMB: Int = Int.MaxValue

  override def getClientContext: ClientContext = MockClientContext

  override def getLogStreamName: String = "MockContext"

  override def getInvokedFunctionArn: String = "MockContext"

  override def getIdentity: CognitoIdentity = MockCognitoIdentity

  override def getLogGroupName: String = "MockContext"

  override def getAwsRequestId: String = "MockContext"
}

object MockLambdaLogger extends LambdaLogger {
  override def log(string: String) = println(string)
}

object MockClientContext extends ClientContext {
  import scala.collection.JavaConverters._
  override def getCustom: java.util.Map[String, String] = {
    Map.empty[String, String].asJava
  }

  override def getEnvironment: java.util.Map[String, String] = {
    Map.empty[String, String].asJava
  }

  override def getClient: Client = MockClient
}

object MockClient extends Client {
  override def getAppPackageName: String = "MockClient"

  override def getInstallationId: String = "MockClient"

  override def getAppTitle: String = "MockClient"

  override def getAppVersionCode: String = "MockClient"

  override def getAppVersionName: String = "MockClient"
}

object MockCognitoIdentity extends CognitoIdentity {
  override def getIdentityId: String = "MockCognitoIdentity"

  override def getIdentityPoolId: String = "MockCognitoIdentity"
}