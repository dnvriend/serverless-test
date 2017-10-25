package hello

import play.api.libs.json.{ Format, Json }
import scalaz._
import scalaz.Scalaz._

object Person {
  implicit val format: Format[Person] = Json.format[Person]
  implicit val validator: Validator[Person] = (data: Person) => {
    List(
      Validator.validateNonEmpty("name", data.name),
      Validator.validateNonZero("age", data.age),
      Validator.validateNonNegative("age", data.age),
      Validator.validateMinMaxInclusive("age", data.age, 0, 110),
    ).sequenceU.rightMap(_ => data).leftMap(_.map(msg => ValidationException(msg)))
  }
}

case class Person(name: String, age: Int)
