package com.github.dnvriend.aws.lambda.handler

import scalaz._
import scalaz.Scalaz._

object Validator {
  def emptyValidator[A](that: A): Validator[A] = (data: A) => {
    Disjunction.right[Throwable, A](data).validationNel[Throwable]
  }

  def validateNonEmpty(fieldName: String, value: String): ValidationNel[String, String] = {
    if (value.trim.isEmpty)
      s"Field '$fieldName' is empty".failureNel[String]
    else
      value.successNel[String]
  }

  def validateNonZero(fieldName: String, value: Long): ValidationNel[String, Long] = {
    if (value == 0) s"Field '$fieldName' with value '$value' may not be zero".failureNel[Long] else value.successNel[String]
  }

  def validateNonNegative(fieldName: String, value: Long): ValidationNel[String, Long] = {
    if (value < 0) s"Field '$fieldName' with value '$value' may not be less than zero".failureNel[Long] else value.successNel[String]
  }

  def validateMinMaxInclusive(fieldName: String, value: Long, min: Long, max: Long): ValidationNel[String, Long] = {
    if (value < min || value > max) s"Field '$fieldName' with value '$value' must be between $min inclusive and $max inclusive".failureNel[Long] else value.successNel[String]
  }
}

trait Validator[A] {
  def validate(data: A): ValidationNel[Throwable, A]
}

case class ValidationException(msg: String) extends RuntimeException(msg)