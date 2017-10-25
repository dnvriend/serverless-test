package hello.it

import hello.{ Person, ServerlessTest }

class PersonDisjunctionNelHandlerIntegrationTest extends ServerlessTest {
  def feature = Feature("Person Disjunction Nel Handler") {
    Scenario("Posting a Person DisjunctionNel") {
      When I post("/dev/persondisjunctionnel").withBody(Person("Dennis", 42))
      Then assert status.is(200)
      And assert body.is(Person("Dennis", 42))
    }
  }
}