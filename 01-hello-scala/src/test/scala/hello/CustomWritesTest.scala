package hello

import play.api.libs.json.Json
import scalaz._
import scalaz.Scalaz._

class CustomWritesTest extends TestSpec with CustomWrites {
  it should "marshal JsonError" in {
    val result: Disjunction[Throwable, Person] = Disjunction.fromTryCatchNonFatal(Json.parse("""{"name":"Dennis", "age":"42"}""").as[Person])
    //    println(Json.toJson(result).toString())
  }

  it should "marshal JsonError nel" in {
    val result: Disjunction[NonEmptyList[Throwable], Person] =
      Disjunction.fromTryCatchNonFatal(Json.parse("""{"name":"Dennis", "age":"42"}""").as[Person])
        .leftMap(_.wrapNel)
    //    println(Json.toJson(result).toString())
  }
}
