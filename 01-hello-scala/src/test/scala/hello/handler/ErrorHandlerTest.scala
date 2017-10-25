package hello.handler

import com.github.dnvriend.aws.lambda.handler.test.TestSpec
import hello.ErrorHandler

class ErrorHandlerTest extends TestSpec {
  it should "return an error" in {
    val result = withApiGatewayRequest[String](None) { is => os => ctx =>
      ErrorHandler.handleRequest(is, os, ctx)
    }
    result.json.statusCode shouldBe 500
    result.json.bodyAs[List[String]].value should contain("Error Handler")
  }
}
