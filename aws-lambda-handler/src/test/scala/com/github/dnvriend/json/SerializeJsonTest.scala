package com.github.dnvriend.json

import com.github.dnvriend.ops.AllOps
import org.scalatest.{ FlatSpec, Matchers }
import play.api.libs.json.Json

class SerializeJsonTest extends FlatSpec with Matchers with AllOps {
  it should "escape the json string" in {
    Json.obj("name" -> "dennis", "age" -> 42).escapedJson.bytes.md5 shouldBe "3755A8786B470E3FE828C5DE1C227240"
  }

  it should "string" in {
    Json.obj("name" -> "dennis", "age" -> 42).bytes.md5 shouldBe "3A28E45FD37A85D41D8154623A2F2C17"
  }

  it should "pretty" in {
    Json.obj("name" -> "dennis", "age" -> 42).bytes.md5 shouldBe "3A28E45FD37A85D41D8154623A2F2C17"
  }
}
