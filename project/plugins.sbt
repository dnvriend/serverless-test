addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")

// code formatter
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.1")

// a rewrite and linting tool
// see: https://scalacenter.github.io/scalafix/
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.5.3")

// dynamodb
// see: https://github.com/localytics/sbt-dynamodb
addSbtPlugin("com.localytics" % "sbt-dynamodb" % "2.0.0")