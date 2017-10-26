package com.github.dnvriend.ops

import com.github.dnvriend.aws.lambda.handler.ValidationException
import com.github.dnvriend.ops.Functional.DisjunctionNel

import scala.language.implicitConversions
import scalaz.Scalaz._
import scalaz._

object Functional {
  type DisjunctionNel[A, B] = Disjunction[NonEmptyList[A], B]
}

trait FunctionalOps {
  implicit def ToFunctionalOpsImpl[A](that: => A) = new FunctionalOpsImpl(that)

  implicit class FEitherOps[F[_]: Functor: Traverse, A, B](that: F[Either[A, B]])(implicit x: Unapply[Applicative, Either[A, B]]) {
    implicit def toSequencedDisjunction: DisjunctionNel[A, F[B]] = {
      that.map(_.disjunction.validationNel).sequenceU.disjunction
    }
  }

  implicit class EitherOps[A, B](that: Either[A, B]) {
    def toDisjunctionNel: DisjunctionNel[A, B] = {
      that.disjunction.validationNel.disjunction
    }
  }
}

class FunctionalOpsImpl[A](that: => A) {
  def safe: Disjunction[Throwable, A] = Disjunction.fromTryCatchNonFatal(that)
  def safeNel: DisjunctionNel[Throwable, A] = Disjunction.fromTryCatchNonFatal(that).leftMap(_.wrapNel)
  def safeMsg(msg: String): DisjunctionNel[Throwable, A] = safe.leftMap(t => ValidationException(s"$msg, because: '${t.getMessage}'").wrapNel.widen[Throwable])
  def ? : Option[A] = Option(that)
  def log(implicit show: Show[A] = null): A = {
    val msg: String = Option(show).map(_.shows(that)).getOrElse(that.toString)
    println(msg)
    that
  }
}
