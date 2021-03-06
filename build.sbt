import laika.ast.Image
import laika.ast.LengthUnit.px
import laika.ast.Path.Root
import laika.helium.Helium
import laika.helium.config.{Favicon, ReleaseInfo, Teaser, TextLink}
import laika.markdown.github.GitHubFlavor
import laika.parse.code.SyntaxHighlighting
import laika.rewrite.nav.CoverImage
import laika.theme.config.Color
import sbt.Keys.libraryDependencies

import java.time.Instant

val townplannerVersion = "1.32.0"
ThisBuild / organization := "com.innovenso.townplanner"
ThisBuild / organizationName := "Innovenso"
ThisBuild / organizationHomepage := Some(url("https://innovenso.com"))
ThisBuild / version := townplannerVersion
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / resolvers += Resolver.mavenLocal
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://bitbucket.org/innovenso/innovenso-townplanner"),
    "scm:git@bitbucket.org:innovenso/innovenso-townplanner.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "jlust",
    name = "Jurgen Lust",
    email = "jurgen@innovenso.com",
    url = url("https://innovenso.com")
  )
)

ThisBuild / description := "The Innovenso Townplanner is a set of libraries used to document a company's enterprise architecture."
ThisBuild / licenses := List(
  "GNU General Public License v3" -> new URL(
    "https://www.gnu.org/licenses/gpl-3.0.txt"
  )
)
ThisBuild / homepage := Some(url("https://townplanner.be"))

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / credentials += Credentials(
  realm = "Sonatype Nexus Repository Manager",
  host = "s01.oss.sonatype.org",
  userName = sys.env.getOrElse("INNOVENSO_PUBLISH_OSSRH_USERNAME", "none"),
  passwd = sys.env.getOrElse("INNOVENSO_PUBLISH_OSSRH_PASSWORD", "none")
)

usePgpKeyHex(sys.env.getOrElse("INNOVENSO_PUBLISH_SIGNING_KEY_ID", "none"))

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
    libraryDependencies += commonsIo,
    libraryDependencies += commonsCodec
  )
lazy val ioDiagrams = project
  .in(file("io-diagrams"))
  .dependsOn(model, ioCore)
  .settings(
    name := "innovenso-townplanner-io-diagram",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem,
    libraryDependencies += plantUml
  )
  .enablePlugins(SbtTwirl)
lazy val ioCharts = project
  .in(file("io-charts"))
  .dependsOn(model, ioCore)
  .settings(
    name := "innovenso-townplanner-io-charts",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem,
    libraryDependencies += jfreeChart,
    libraryDependencies += jfreeSvg
  )
lazy val ioOpenExchange = project
  .in(file("io-openexchange"))
  .dependsOn(model, ioCore)
  .settings(
    name := "innovenso-townplanner-io-openexchange",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem,
    libraryDependencies += scalaXml
  )
lazy val ioLatexCommon = project
  .in(file("io-latex-common"))
  .dependsOn(model, ioCore, ioDiagrams)
  .settings(
    name := "innovenso-townplanner-io-latex-common",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem
  )
  .enablePlugins(SbtTwirl)
lazy val ioLatexPictures = project
  .in(file("io-latex-pictures"))
  .dependsOn(model, ioCore, ioLatexCommon)
  .settings(
    name := "innovenso-townplanner-io-latex-pictures",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem
  )
  .enablePlugins(SbtTwirl)
lazy val ioLatexDocument = project
  .in(file("io-latex-document"))
  .dependsOn(model, ioCore, ioDiagrams, ioLatexCommon, ioLatexPictures)
  .settings(
    name := "innovenso-townplanner-io-latex-document",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem
  )
  .enablePlugins(SbtTwirl)
lazy val ioLatexSlides = project
  .in(file("io-latex-slides"))
  .dependsOn(model, ioCore, ioDiagrams, ioLatexCommon, ioLatexPictures)
  .settings(
    name := "innovenso-townplanner-io-latex-slides",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem
  )
  .enablePlugins(SbtTwirl)
lazy val ioWebsite = project
  .in(file("io-website"))
  .dependsOn(model, ioCore, ioDiagrams)
  .settings(
    name := "innovenso-townplanner-io-website",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest,
    libraryDependencies += lorem,
    libraryDependencies += laikaPdf,
    libraryDependencies += json
  )
  .enablePlugins(SbtTwirl)

lazy val application = project
  .in(file("application"))
  .dependsOn(
    model,
    ioCore,
    ioDiagrams,
    ioWebsite,
    ioLatexCommon,
    ioLatexPictures,
    ioLatexDocument,
    ioLatexSlides,
    ioOpenExchange
  )
  .settings(
    name := "innovenso-townplanner-application",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest
  )
lazy val sample = project
  .in(file("sample"))
  .dependsOn(application)
  .settings(
    name := "innovenso-townplanner-sample",
    libraryDependencies += scalactic,
    libraryDependencies += scalaTest
  )
lazy val root = project
  .in(file("docs"))
  .settings(
    name := "innovenso-townplanner-docs",
    version := version.value,
    scalaVersion := scalaVersion.value,
    laikaIncludeEPUB := true,
    laikaIncludePDF := true,
    laikaTheme := theme,
    laikaExtensions ++= Seq(GitHubFlavor, SyntaxHighlighting),
    laikaConfig := LaikaConfig.defaults
      .withConfigValue("project.version", version.value)
  )
  .enablePlugins(LaikaPlugin)

val scalactic = "org.scalactic" %% "scalactic" % "3.2.12"
val scalaTest = "org.scalatest" %% "scalatest" % "3.2.12" % "test"
val commonsText = "org.apache.commons" % "commons-text" % "1.9"
val lorem = "com.thedeanda" % "lorem" % "2.1"
val commonsIo = "commons-io" % "commons-io" % "2.11.0"
val plantUml = "net.sourceforge.plantuml" % "plantuml" % "1.2022.5"
val json = "net.liftweb" %% "lift-json" % "3.5.0"
val commonsCodec = "commons-codec" % "commons-codec" % "1.15"
val laikaPdf = "org.planet42" %% "laika-pdf" % "0.18.2"
val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.3.0"
val jfreeChart = "org.jfree" % "jfreechart" % "1.5.3"
val jfreeSvg = "org.jfree" % "jfreesvg" % "3.4.2"

val montserratURL = "https://fonts.googleapis.com/css?family=Montserrat"

val theme = Helium.defaults.all
  .themeColors(
    primary = Color.rgb(37, 26, 6),
    secondary = Color.rgb(8, 136, 140),
    primaryMedium = Color.rgb(77, 77, 77),
    primaryLight = Color.rgb(222, 212, 196),
    text = Color.rgb(37, 26, 6),
    background = Color.rgb(255, 255, 255),
    bgGradient = (Color.rgb(222, 212, 196), Color.rgb(244, 233, 215))
  )
  .site
  .landingPage(
    logo = Some(
      Image.internal(
        Root / "images" / "square.png",
        width = Some(px(200)),
        height = Some(px(200)),
        alt = Some("Innovenso Logo")
      )
    ),
    title = Some("Innovenso Townplanner"),
    subtitle = Some("Enterprise Architecture as Code"),
    license = Some("GPL v3"),
    latestReleases =
      List(ReleaseInfo("Latest Release", s"${townplannerVersion}")),
    documentationLinks = Seq(
      TextLink.internal(Root / "about.md", "About the project"),
      TextLink.internal(Root / "usage.md", "Getting Started"),
      TextLink.internal(Root / "c4model.md", "C4 Model using Townplanner DSL"),
      TextLink.internal(Root / "concepts.md", "Core Concepts"),
      TextLink.internal(Root / "concepts.md", "Roadmap")
    ),
    projectLinks = Seq(
      TextLink.external(
        "https://bitbucket.org/innovenso/innovenso-townplanner",
        "Source on Bitbucket"
      ),
      TextLink.external("https://innovenso.com/", "Innovenso")
    ),
    teasers = Seq(
      Teaser(
        "Rich Model",
        "The model spans all levels of Enterprise Architecture, including business capability maps, pace layering, target operating models, decision records, project impacts, team topologies, infrastructure, applications, technologies, and more. It even has a time machine!"
      ),
      Teaser(
        "Export to Everything",
        "Currently diagram renders to SVG and exports to Confluence, Excel and Archimate are the most useful ones. New exports are easy to add."
      ),
      Teaser(
        "Domain Specific Language",
        "Use your favourite IDE to edit the town plan in an easy to understand DSL, and enjoy code completion and compile-time integrity checks."
      ),
      Teaser(
        "Architecture as Code",
        "The model is just text, so it can be managed with Git, with all the joys of version history, pull requests, branches, ..."
      ),
      Teaser(
        "Opinionated",
        "Loosely based on C4 and Archimate, but a model reflecting the Innovenso metamodel currently. It will grow over time, and it might become more open, as the need arises."
      )
    )
  )
  .site
  .favIcons(
    Favicon.internal(Root / "images" / "favicon.png", sizes = "57x57"),
    Favicon.internal(Root / "images" / "favicon72.png", sizes = "72x72"),
    Favicon.internal(Root / "images" / "favicon72.png", sizes = "114x114")
  )
  .site
  .downloadPage(
    title = "Documentation Downloads",
    description = None,
    downloadPath = Root / "downloads",
    includeEPUB = true,
    includePDF = true
  )
  .epub
  .navigationDepth(4)
  .pdf
  .navigationDepth(4)
  .epub
  .coverImages(CoverImage(Root / "images" / "cover.png"))
  .pdf
  .coverImages(CoverImage(Root / "images" / "cover.png"))
  .all
  .metadata(
    title = Some("Innovenso Townplanner"),
    version = Some(townplannerVersion),
    description = Some("Enterprise Architecture as Code"),
    authors = Seq("Jurgen Lust"),
    language = Some("en"),
    date = Some(Instant.now)
  )
  .build
