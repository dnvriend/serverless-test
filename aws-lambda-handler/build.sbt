name := "aws-lambda-handler"

organization := "com.github.dnvriend"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.3"

libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "1.3.0"
libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.16"
libraryDependencies += "net.jcazevedo" %% "moultingyaml" % "0.4.0"
libraryDependencies += "com.github.agourlay" %% "cornichon" % "0.13.0"
libraryDependencies += "org.typelevel" %% "scalaz-scalatest" % "1.1.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings")

parallelExecution in Test := false

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

// Scalariform settings
SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DanglingCloseParenthesis, Preserve)

