package hello.it

import hello.{ Person, ServerlessTest }

class PersonValidationNelHandlerIntegrationTest extends ServerlessTest {
  def feature = Feature("Person ValidationNel Handler") {
    Scenario("Posting a Person ValidationNel") {
      When I post("/dev/personvalidationnel").withBody(Person("Dennis", 42))
      Then assert status.is(200)
      And assert body.is(Person("Dennis", 42))
    }
  }
}