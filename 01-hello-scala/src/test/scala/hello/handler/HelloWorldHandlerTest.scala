package hello.handler

import com.github.dnvriend.aws.lambda.handler.test.TestSpec
import hello.{ HelloMessage, HelloWorldHandler }

class HelloWorldHandlerTest extends TestSpec {
  it should "return HelloWorld" in {
    val result = withApiGatewayRequest[String](None) { is => os => ctx =>
      HelloWorldHandler.handleRequest(is, os, ctx)
    }
    result.json.statusCode shouldBe 200
    result.json.bodyAs[HelloMessage].value shouldBe HelloMessage("Hello World!")
  }
}
