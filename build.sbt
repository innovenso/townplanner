val scala2Version = "2.13.8"

lazy val root = project
  .in(file("."))
  .settings(
    organization := "com.innovenso.townplanner",
    name := "innovenso-townplanner-model",
    version := "1.0.1",
    scalaVersion := scala2Version,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10",
    libraryDependencies += "org.apache.commons" % "commons-text" % "1.9",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    libraryDependencies += "com.thedeanda" % "lorem" % "2.1" % Test,
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
  )
