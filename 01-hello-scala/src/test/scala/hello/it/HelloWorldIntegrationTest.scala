package hello.it

import com.github.dnvriend.aws.lambda.handler.test.ServerlessTest

class HelloWorldIntegrationTest extends ServerlessTest {
  def feature = Feature("HelloWorldHandler") {
    Scenario("call the hello endpoint") {
      When I get("/dev/hello")
      Then assert status.is(200)
      And assert body.path("message").is("Hello World!")
    }
  }
}
