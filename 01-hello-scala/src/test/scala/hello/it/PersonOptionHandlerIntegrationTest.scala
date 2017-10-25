package hello.it

import com.github.dnvriend.aws.lambda.handler.test.ServerlessTest
import hello.Person

class PersonOptionHandlerIntegrationTest extends ServerlessTest {
  def feature = Feature("Person Option Handler") {
    Scenario("Posting a Person Option") {
      When I post("/dev/personoption").withBody(Person("Dennis", 42))
      Then assert status.is(200)
      And assert body.is(Person("Dennis", 42))
    }
  }
}