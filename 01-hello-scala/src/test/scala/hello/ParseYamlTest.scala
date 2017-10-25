//package hello
//
//import net.jcazevedo.moultingyaml._
//import net.jcazevedo.moultingyaml.DefaultYamlProtocol._
//import scalaz._
//import scalaz.Scalaz._
//
////class ParseYamlTest extends TestSpec {
////  //  val yamlstring =
////  //    """
////  //      |service: helloworld-scala
////  //      |stage: dev
////  //      |region: us-east-1
////  //      |stack: helloworld-scala-dev
////  //      |api keys:
////  //      |  None
////  //      |endpoints:
////  //      |  GET - https://g6qb1n0hq7.execute-api.us-east-1.amazonaws.com/dev/error
////  //      |  GET - https://g6qb1n0hq7.execute-api.us-east-1.amazonaws.com/dev/hello
////  //      |  POST - https://g6qb1n0hq7.execute-api.us-east-1.amazonaws.com/dev/persondisjunction
////  //      |  POST - https://g6qb1n0hq7.execute-api.us-east-1.amazonaws.com/dev/persondisjunctionnel
////  //      |  POST - https://g6qb1n0hq7.execute-api.us-east-1.amazonaws.com/dev/personoption
////  //      |  POST - https://g6qb1n0hq7.execute-api.us-east-1.amazonaws.com/dev/personvalidation
////  //      |  POST - https://g6qb1n0hq7.execute-api.us-east-1.amazonaws.com/dev/personvalidationnel
////  //      |functions:
////  //      |  ErrorHandler: helloworld-scala-dev-ErrorHandler
////  //      |  HelloHandler: helloworld-scala-dev-HelloHandler
////  //      |  PersonDisjunctionHandler: helloworld-scala-dev-PersonDisjunctionHandler
////  //      |  PersonDisjunctionNelHandler: helloworld-scala-dev-PersonDisjunctionNelHandler
////  //      |  PersonOptionHandler: helloworld-scala-dev-PersonOptionHandler
////  //      |  PersonValidationHandler: helloworld-scala-dev-PersonValidationHandler
////  //      |  PersonValidationNelHandler: helloworld-scala-dev-PersonValidationNelHandler
////  //    """.stripMargin
////  //
////  //  val fields = yamlstring.parseYaml.asYamlObject.fields
////  //  //val functions = fields.get("functions".toYaml).map(_.convertTo[Map[String, String]]).map(_.keys.toList).toString.log
////
////  import scala.sys.process._
////
////  val serviceInfo: String = "sls info".lineStream_!.dropWhile(_ != "Service Information").toList.drop(1).mkString("\n")
////  serviceInfo.parseYaml
////  val fields = serviceInfo.parseYaml.asYamlObject.fields
////  val urls = fields.get("endpoints".toYaml).map(_.convertTo[String])
////    .map(_.split(" - "))
////    .map(_.toList
////      .map(_.replace(" GET", ""))
////      .map(_.replace(" POST", ""))
////      .map(_.trim)
////      .filterNot(_ == "GET")
////      .filterNot(_ == "POST")
////    ).toString.log
//}
