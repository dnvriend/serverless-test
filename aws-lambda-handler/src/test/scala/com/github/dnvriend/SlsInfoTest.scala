package com.github.dnvriend

import com.github.dnvriend.aws.lambda.handler.test.TestSpec

object SlsInfoTest {
  final val SlsInfo =
    """
      |Service Information
      |service: helloworld-scala
      |stage: dev
      |region: eu-central-1
      |stack: helloworld-scala-dev
      |api keys:
      |  None
      |endpoints:
      |  GET - https://5jaorhjpz1.execute-api.eu-central-1.amazonaws.com/dev/error
      |  GET - https://5jaorhjpz1.execute-api.eu-central-1.amazonaws.com/dev/hello
      |  POST - https://5jaorhjpz1.execute-api.eu-central-1.amazonaws.com/dev/persondisjunction
      |  POST - https://5jaorhjpz1.execute-api.eu-central-1.amazonaws.com/dev/persondisjunctionnel
      |  POST - https://5jaorhjpz1.execute-api.eu-central-1.amazonaws.com/dev/personoption
      |  POST - https://5jaorhjpz1.execute-api.eu-central-1.amazonaws.com/dev/personvalidation
      |  POST - https://5jaorhjpz1.execute-api.eu-central-1.amazonaws.com/dev/personvalidationnel
      |functions:
      |  ErrorHandler: helloworld-scala-dev-ErrorHandler
      |  HelloHandler: helloworld-scala-dev-HelloHandler
      |  PersonDisjunctionHandler: helloworld-scala-dev-PersonDisjunctionHandler
      |  PersonDisjunctionNelHandler: helloworld-scala-dev-PersonDisjunctionNelHandler
      |  PersonOptionHandler: helloworld-scala-dev-PersonOptionHandler
      |  PersonValidationHandler: helloworld-scala-dev-PersonValidationHandler
      |  PersonValidationNelHandler: helloworld-scala-dev-PersonValidationNelHandler
    """.stripMargin
}

// see: https://www.tutorialspoint.com/scala/scala_regular_expressions.htm
class SlsInfoTest extends TestSpec {
  it should "find the base url using regex" in {
    SlsInfoTest.SlsInfo.find("""https://(.+).amazonaws.com""".r).value shouldBe "https://5jaorhjpz1.execute-api.eu-central-1.amazonaws.com"
  }
}
