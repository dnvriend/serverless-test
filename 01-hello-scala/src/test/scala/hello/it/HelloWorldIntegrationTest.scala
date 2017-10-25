package hello.it

import hello.ServerlessTest

class HelloWorldIntegrationTest extends ServerlessTest {
  def feature = Feature("HelloWorldHandler") {
    Scenario("call the hello endpoint") {
      When I get(getEndpoint("/hello").get)
      Then assert status.is(200)
      And assert body.path("message").is("Hello World!")
    }
  }
}
