package hello.it

import com.github.dnvriend.aws.lambda.handler.test.ServerlessTest
import hello.Person

class PersonDisjunctionNelHandlerIntegrationTest extends ServerlessTest {
  def feature = Feature("Person Disjunction Nel Handler") {
    Scenario("Posting a Person DisjunctionNel") {
      When I post("/dev/persondisjunctionnel").withBody(Person("Dennis", 42))
      Then assert status.is(200)
      And assert body.is(Person("Dennis", 42))
    }
  }
}