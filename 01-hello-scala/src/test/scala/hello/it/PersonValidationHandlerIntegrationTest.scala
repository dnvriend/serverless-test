package hello.it

import com.github.dnvriend.aws.lambda.handler.test.ServerlessTest
import hello.Person

class PersonValidationHandlerIntegrationTest extends ServerlessTest {
  def feature = Feature("Person Validation Handler") {
    Scenario("Posting a Person Validation") {
      When I post("/dev/personvalidation").withBody(Person("Dennis", 42))
      Then assert status.is(200)
      And assert body.is(Person("Dennis", 42))
    }
  }
}