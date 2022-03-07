package com.wayneenterprises.townplan.technology

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{Framework, Language, Platform}
import com.innovenso.townplanner.model.concepts.properties.{BeEliminated, BeInvestedIn, BeTolerated, Description}

case class TechnologyRadar()(implicit ea: EnterpriseArchitecture) {
  val java: Language = ea describes Language(title = "Java") as { it =>
    it has Description("Java is a high-level, class-based, object-oriented programming language.")
    it should BeInvestedIn("Although other programming languages have become popular the last few years, we still consider Java to be the default choice for backend development, due to its rich ecosystem, robustness and availability of developers")
  }

  val kubernetes: Platform = ea describes Platform(title = "Kubernetes") as { it =>
    it has Description("Kubernetes is an open-source container orchestration system for automating software deployment, scaling, and management.")
    it should BeInvestedIn("For now, Kubernetes is the best bet for cloud applications, since it is supported by all major public clouds and as such leaves the door open for a multi-cloud strategy.")
  }

  val react: Framework = ea describes Framework(title = "React") as { it =>
    it has Description("React is a free and open-source front-end JavaScript library[3] for building user interfaces based on UI components.")
    it should BeTolerated("At Wayne Enterprises we prefer to take the HTML/CSS-first approach, rather than the Javascript-first approach made popular by SPA in recent years.")
  }

  val mongodb: Platform = ea describes Platform(title = "MongoDB") as { it =>
    it has Description("MongoDB is a source-available cross-platform document-oriented database program.")
    it should BeEliminated("As we are moving to a fully event-sourced architecture, we prefer a databaseless approach.")
  }
}
