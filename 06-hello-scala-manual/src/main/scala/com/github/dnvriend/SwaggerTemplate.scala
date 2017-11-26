package com.github.dnvriend

import java.io.File

import com.github.dnvriend.ops.AllOps
import play.api.libs.json.{ Json, _ }

object Monoid {
  def apply[A](implicit m: Monoid[A]): Monoid[A] = m
  def instance[A](z: A, f: (A, A) => A): Monoid[A] = new Monoid[A] {
    override def zero: A = z
    override def append(a1: A, a2: A) = f(a1, a2)
  }

  // 0 + 0 = 0
  // 1 + 0 = 1
  // 0 + 1 = 1
  // 1 + 1 = 2
  implicit val JsValMonoid: Monoid[JsValue] = instance(JsNull, {
    case (JsNull, JsNull)           => JsNull
    case (x, JsNull)                => x
    case (JsNull, y)                => y
    case (x: JsObject, y: JsObject) => x ++ y
  })
}

trait Monoid[A] {
  def zero: A
  def append(a1: A, a2: A): A
}

object Path {
  implicit val writes: Writes[Path] = new Writes[Path] {
    override def writes(p: Path): JsValue = {
      Json.obj(
        p.path -> Json.obj(
          p.method -> Json.obj(
            "description" -> p.description
          ),
          "responses" -> Json.obj(
            p.responseCode -> Json.obj(
              "schema" -> Json.obj(
                "type" -> "String"
              )
            )
          )
        )
      )
    }
  }
}

case class Path(path: String, method: String, description: String, responseCode: String)

object SwaggerTemplate extends AllOps {

  def swagger(): JsObject = {
    info("HelloWorldApi") ++
      schemes() ++
      basePath("/dev") ++
      swaggerVersion() ++
      paths(List(Path("/hello", "get", "hello-api", "200")))
  }

  def info(title: String): JsObject = {
    Json.obj(
      "info" -> Json.obj(
        "title" -> title
      ))
  }
  def schemes(): JsObject = {
    Json.obj(
      "schemes" -> Json.arr("https")
    )
  }
  def basePath(basePath: String = "/dev"): JsObject = {
    Json.obj(
      "basePath" -> basePath
    )
  }
  def swaggerVersion(version: String = "2.0"): JsObject = {
    Json.obj(
      "swagger" -> version
    )
  }
  def paths(paths: List[Path]): JsObject = {
    Json.obj(
      "paths" -> foldPaths(paths)
    )
  }

  def foldPaths(xs: List[Path])(implicit m: Monoid[JsValue], w: Writes[Path]): JsObject = {
    xs.foldLeft(m.zero)((a1, a2) => m.append(a1, Json.toJson(a2))) match {
      case JsNull      => Json.obj()
      case x: JsObject => x
    }
  }

  def main(args: Array[String]): Unit = {
    val swag = Json.prettyPrint(swagger())
    println(swag)
    new File("/tmp/swagger.json").write(swag)
  }
}
