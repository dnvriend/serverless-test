package hello.handler

import hello.{ Person, PersonDisjunctionHandler, TestSpec }

class PersonDisjunctionHandlerTest extends TestSpec {
  it should "return Person when posted" in {
    val result = withApiGatewayRequest[Person](Some(Person("Dennis", 42))) { is => os => ctx =>
      PersonDisjunctionHandler.handleRequest(is, os, ctx)
    }
    result.json.statusCode shouldBe 200
    result.json.bodyAs[Person].value shouldBe Person("Dennis", 42)
  }
}
