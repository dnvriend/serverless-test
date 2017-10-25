package hello.it

import com.github.dnvriend.aws.lambda.handler.test.ServerlessTest
import hello.Person

class PersonDisjunctionHandlerIntegrationTest extends ServerlessTest {
  def feature = Feature("Person Disjunction Handler") {
    Scenario("Posting a Person Disjunction") {
      When I post("/dev/persondisjunction").withBody(Person("Dennis", 42))
      //      And I show_session
      //      And I show_last_status
      //      And I show_last_response
      //      And I show_last_headers
      Then assert status.is(200)
      And assert body.is(Person("Dennis", 42))
      //      And assert body.path("name").is("Dennis")
      //      And assert body.path("age").is(42)
    }
  }
}