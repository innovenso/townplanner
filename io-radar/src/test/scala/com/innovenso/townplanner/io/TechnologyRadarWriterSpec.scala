package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.{Framework, Language, Platform}
import com.innovenso.townplanner.model.concepts.properties.{
  BeEliminated,
  BeInvestedIn,
  BeTolerated,
  Description
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class TechnologyRadarWriterSpec extends AnyFlatSpec with GivenWhenThen {
  "JSON output" should "be written for the technology radar" in new RadarIO {
    When("some technologies")
    (1 to 10).foreach { it =>
      samples.language
      samples.framework
      samples.tool
      samples.technique
      samples.platformTechnology
    }
    Then("the technology radar is written to JSON")
    assert(jsonIsWritten)
  }

  "Technology Radar Site" should "be generated" in new RadarIO {
    When("some technologies")
    val java: Language = ea describes Language(title = "Java") as { it =>
      it has Description(
        "Java is a high-level, class-based, object-oriented programming language."
      )
      it should BeInvestedIn(
        "Although other programming languages have become popular the last few years, we still consider Java to be the default choice for backend development, due to its rich ecosystem, robustness and availability of developers"
      )
    }

    val kubernetes: Platform = ea describes Platform(title = "Kubernetes") as {
      it =>
        it has Description(
          "Kubernetes is an open-source container orchestration system for automating software deployment, scaling, and management."
        )
        it should BeInvestedIn(
          "For now, Kubernetes is the best bet for cloud applications, since it is supported by all major public clouds and as such leaves the door open for a multi-cloud strategy."
        )
    }

    val react: Framework = ea describes Framework(title = "React") as { it =>
      it has Description(
        "React is a free and open-source front-end JavaScript library for building user interfaces based on UI components."
      )
      it should BeTolerated(
        "At Wayne Enterprises we prefer to take the HTML/CSS-first approach, rather than the Javascript-first approach made popular by SPA in recent years."
      )
    }

    val mongodb: Platform = ea describes Platform(title = "MongoDB") as { it =>
      it has Description(
        "MongoDB is a source-available cross-platform document-oriented database program."
      )
      it should BeEliminated(
        "As we are moving to a fully event-sourced architecture, we prefer a databaseless approach."
      )
    }
    samples.tool
    samples.technique
    When("some technologies")
    Then("the technology radar is generated")
    assert(siteIsGenerated)
  }
}
