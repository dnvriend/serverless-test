package hello.handler

import com.github.dnvriend.aws.lambda.handler.test.TestSpec
import hello.{Person, PersonValidationNelHandler}

class PersonValidationNelHandlerTest extends TestSpec {
  it should "return Person when posted" in {
    val result = withApiGatewayRequest[Person](Some(Person("Dennis", 42))) { is => os => ctx =>
      PersonValidationNelHandler.handleRequest(is, os, ctx)
    }
    result.json.statusCode shouldBe 200
    result.json.bodyAs[Person].value shouldBe Person("Dennis", 42)
  }

  it should "return an error" in {
    val result = withApiGatewayRequest[Person](Some(Person("", -1))) { is => os => ctx =>
      PersonValidationNelHandler.handleRequest(is, os, ctx)
    }

    result.json.statusCode shouldBe 500
    result.json.bodyAs[List[String]].value should contain allOf (
      "Field 'name' is empty",
      "Field 'age' with value '-1' may not be less than zero",
      "Field 'age' with value '-1' must be between 0 inclusive and 110 inclusive",
    )
  }
}
