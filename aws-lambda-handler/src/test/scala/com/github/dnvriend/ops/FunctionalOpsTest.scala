package com.github.dnvriend.ops

import org.scalatest.{ FlatSpec, Matchers }
import org.typelevel.scalatest.DisjunctionMatchers

class FunctionalOpsTest extends FlatSpec with Matchers with FunctionalOps with DisjunctionMatchers {
  it should "normally exceptions are thrown" in {
    intercept[Throwable] {
      1 / 0
    }
  }

  it should "handle exceptions safely" in {
    (1 / 0).safe shouldBe left[Throwable]
  }

  it should "handle exceptions safely with message" in {
    (1 / 0).safeMsg("Help!").log
  }
}
