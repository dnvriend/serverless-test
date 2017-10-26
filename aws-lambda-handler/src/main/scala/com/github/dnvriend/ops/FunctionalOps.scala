package com.github.dnvriend.ops

import com.github.dnvriend.ops.Functional.DisjunctionNel

import scala.language.implicitConversions
import scalaz.Scalaz._
import scalaz._

object Functional {
  type DisjunctionNel[A, B] = Disjunction[NonEmptyList[A], B]
}

trait FunctionalOps {
  implicit def ToFunctionalOpsImpl[A](that: => A) = new FunctionalOpsImpl(that)
}

class FunctionalOpsImpl[A](that: => A) {
  def safe: Disjunction[Throwable, A] = Disjunction.fromTryCatchNonFatal(that)
  def safeNel: DisjunctionNel[Throwable, A] = Disjunction.fromTryCatchNonFatal(that).leftMap(_.wrapNel)
  def ? : Option[A] = Option(that)
}
