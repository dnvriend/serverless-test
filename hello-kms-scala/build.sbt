organization := "com.github.dnvriend"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.3"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.235"
libraryDependencies += "com.amazonaws" % "aws-encryption-sdk-java" % "1.3.1"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.7"
libraryDependencies += "com.sksamuel.avro4s" %% "avro4s-core" % "1.8.0"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.16"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-feature",
  "-Xfatal-warnings",
)

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

// Scalariform settings
SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DanglingCloseParenthesis, Preserve)

