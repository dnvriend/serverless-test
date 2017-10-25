package hello.handler

import hello.{ Person, PersonDisjunctionNelHandler, TestSpec }

class PersonDisjunctionNelHandlerTest extends TestSpec {
  it should "return Person when posted" in {
    val result = withApiGatewayRequest[Person](Some(Person("Dennis", 42))) { is => os => ctx =>
      PersonDisjunctionNelHandler.handleRequest(is, os, ctx)
    }
    result.json.statusCode shouldBe 200
    result.json.bodyAs[Person].value shouldBe Person("Dennis", 42)
  }
}
