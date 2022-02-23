ThisBuild / organization := "com.innovenso.townplanner"
ThisBuild / version := "1.0.5"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / resolvers += Resolver.mavenLocal

lazy val model = project
  .in(file("model"))
  .settings(
    name := "innovenso-townplanner-model",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem,
    libraryDependencies += commonsText
  )
lazy val ioCore = project
  .in(file("io-core"))
  .dependsOn(model)
  .settings(
    name := "innovenso-townplanner-io-core",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem,
    libraryDependencies += commonsIo
  )
lazy val ioDiagrams = project
  .in(file("io-diagrams"))
  .dependsOn(model, ioCore)
  .settings(
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem,
    libraryDependencies += plantUml,
    libraryDependencies += scalaLogging
  )
  .enablePlugins(SbtTwirl)
val scalactic = "org.scalactic" %% "scalactic" % "3.2.11"
val scalaTest = "org.scalatest" %% "scalatest" % "3.2.11" % "test"
val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
val commonsText = "org.apache.commons" % "commons-text" % "1.9"
val lorem = "com.thedeanda" % "lorem" % "2.1" % Test
val commonsIo = "commons-io" % "commons-io" % "2.11.0"
val plantUml = "net.sourceforge.plantuml" % "plantuml" % "1.2021.16"
