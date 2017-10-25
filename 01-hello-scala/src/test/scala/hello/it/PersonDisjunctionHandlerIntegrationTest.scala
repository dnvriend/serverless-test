package hello.it

import hello.{ Person, ServerlessTest }
import play.api.libs.json.Json

class PersonDisjunctionHandlerIntegrationTest extends ServerlessTest {
  def feature = Feature("Person Disjunction Handler") {
    Scenario("Posting a Person") {
      When I post(getEndpoint("/persondisjunction").get).withBody(Json.toJson(Person("Dennis", 42)).toString)
      Then assert status.is(200)
      And assert body.path("name").is("Dennis")
      And assert body.path("age").is(42)
    }
  }
}