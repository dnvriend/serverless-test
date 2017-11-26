name := "06-hello-scala-manual"

scalaVersion := "2.12.4"

libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.2.0"
libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "2.0.2"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.7"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.16"
libraryDependencies += "org.typelevel" %% "scalaz-scalatest" % "1.1.2" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test

scalacOptions ++= Seq(
//  "-unchecked",
//  "-deprecation",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-feature",
//  "-Xfatal-warnings",
  "-target:jvm-1.8",
)

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

// Scalariform settings
SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DanglingCloseParenthesis, Preserve)

// native packager
// http://sbt-native-packager.readthedocs.io/en/stable/formats/universal.html#
topLevelDirectory := None
artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + artifact.extension
}
//packageBin in Universal := {
//  val originalFileName = (packageBin in Universal).value
//  val (base, ext) = originalFileName.baseAndExt
//  val newFileName = file(originalFileName.getParent) / (base + "_dist." + ext)
//  IO.move(originalFileName, newFileName)
//  newFileName
//}

enablePlugins(JavaAppPackaging)
