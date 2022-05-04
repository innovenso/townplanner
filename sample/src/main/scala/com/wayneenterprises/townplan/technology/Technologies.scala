package com.wayneenterprises.townplan.technology

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{
  Framework,
  Language,
  Platform,
  Technique,
  Tool
}
import com.innovenso.townplanner.model.concepts.properties.{
  BeEliminated,
  BeInvestedIn,
  BeMigrated,
  BeTolerated,
  Description,
  Website
}
import com.innovenso.townplanner.model.concepts.relationships.{
  Expert,
  HighlyKnowledgeable,
  Knowledgeable,
  Learner,
  NoKnowledge
}
import com.wayneenterprises.townplan.business.Actors

case class Technologies()(implicit ea: EnterpriseArchitecture, actors: Actors) {
  val java: Language = ea describes Language(title = "Java") as { it =>
    it has Description(
      "Java is a high-level, class-based, object-oriented programming language."
    )
    it should BeInvestedIn(
      "Although other programming languages have become popular the last few years, we still consider Java to be the default choice for backend development, due to its rich ecosystem, robustness and availability of developers"
    )
    it isKnownBy (target = actors.bruceWayne, level = Expert)
    it isKnownBy (target = actors.clarkKent, level = Learner)
  }

  val kubernetes: Platform = ea describes Platform(title = "Kubernetes") as {
    it =>
      it has Description(
        "Kubernetes is an open-source container orchestration system for automating software deployment, scaling, and management."
      )
      it should BeInvestedIn(
        "For now, Kubernetes is the best bet for cloud applications, since it is supported by all major public clouds and as such leaves the door open for a multi-cloud strategy."
      )
      it isKnownBy (target = actors.bruceWayne, level = Expert)
      it isKnownBy (target = actors.clarkKent, level = Knowledgeable)
  }

  val react: Framework = ea describes Framework(title = "React") as { it =>
    it has Description(
      "React is a free and open-source front-end JavaScript library[3] for building user interfaces based on UI components."
    )
    it should BeTolerated(
      "At Wayne Enterprises we prefer to take the HTML/CSS-first approach, rather than the Javascript-first approach made popular by SPA in recent years."
    )
    it isKnownBy (target = actors.bruceWayne, level = Expert)
    it isKnownBy (target = actors.clarkKent, level = NoKnowledge)
  }

  val mongodb: Platform = ea describes Platform(title = "MongoDB") as { it =>
    it has Description(
      "MongoDB is a source-available cross-platform document-oriented database program."
    )
    it should BeEliminated(
      "As we are moving to a fully event-sourced architecture, we prefer a databaseless approach."
    )
    it isKnownBy (target = actors.bruceWayne, level = Expert)
    it isKnownBy (target = actors.clarkKent, level = HighlyKnowledgeable)
  }

  val mobbing: Technique = ea describes Technique(title = "Mobbing") as { it =>
    it has Description(
      "Mob programming (sometimes informally called mobbing or ensemble programming) is a software development approach where the whole team works on the same thing, at the same time, in the same space, and at the same computer."
    )
    it should BeInvestedIn(
      "mob programming typically results in better, more readable code. It can actually speed up development and it makes sure decisions are shared by the team."
    )
    it isKnownBy actors.justiceLeague
    it isKnownBy actors.robin
    it isKnownBy (target = actors.bruceWayne, level = Expert)
    it isKnownBy (target = actors.clarkKent, level = Expert)
  }

  val townplanner: Tool =
    ea describes Tool(title = "Innovenso Townplanner") as { it =>
      it has Description("Enterprise Architecture as Code")
      it should BeInvestedIn(
        "Rather than struggling with hundreds of complicated pop-up screens in traditional EA tools, the Townplanner allows architects to model the enterprise architecture using a DSL."
      )
      it isKnownBy actors.bruceWayne
      it isKnownBy actors.robin
      it isKnownBy actors.clarkKent
    }

  val batMobile: Tool = ea describes Tool(title = "Batmobile") as { it =>
    it should BeInvestedIn()
    it isKnownBy actors.bruceWayne
  }

  val lightHouse: Tool = ea describes Tool(title = "Lighthouse") as { it =>
    it has Description(
      "Lighthouse is a tool written by Google to assess web applications and web pages, collecting performance metrics and insights on good development practices."
    )
    it should BeInvestedIn(
      "Performance testing should be included in any pipeline."
    )
    it isKnownBy actors.bruceWayne
    it isKnownBy actors.clarkKent
  }

  val infrastructureAsCode: Technique =
    ea describes Technique(title = "Infrastructure as Code") as { it =>
      it has Description(
        "Infrastructure as code (IaC) is the process of managing and provisioning computer data centers through machine-readable definition files, rather than physical hardware configuration or interactive configuration tools. The IT infrastructure managed by this process comprises both physical equipment, such as bare-metal servers, as well as virtual machines, and associated configuration resources. The definitions may be in a version control system. It can use either scripts or declarative definitions, rather than manual processes, but the term is more often used to promote declarative approaches."
      )
      it should BeInvestedIn(
        "IaC is an essential part of SecDevOps and should be the only way to set up infrastructure in the cloud."
      )
    }

  val graphQL: Language = ea describes Language(title = "GraphQL") as { it =>
    it has Description(
      "GraphQL is an open-source data query and manipulation language for APIs, and a runtime for fulfilling queries with existing data."
    )
    it has Description(
      "It allows clients to define the structure of the data required, and the same structure of the data is returned from the server, therefore preventing excessively large amounts of data from being returned. The downside of this is the effectiveness of caching."
    )
    it should BeInvestedIn(
      "GraphQL works very well in Backends-for-Frontend (BFF) of dynamic websites."
    )
  }

  val pact: Tool = ea describes Tool(title = "Pact.IO") as { it =>
    it has Description(
      "PACT is an open source CDC testing framework. It also provides a wide range of language supports like Ruby, Java, Scala, .NET, Javascript, Swift/Objective-C."
    )
    it has Website("https://pact.io/")
    it should BeInvestedIn(
      "Consumer-Driven Contract Testing is a good idea to ensure API compatibility within an organisation. Pact.IO is one of the most mature tools for CDC"
    )
  }

  val cdc: Technique =
    ea describes Technique(title = "Consumer Driven Contracts") as { it =>
      it has Description(
        "Consumer-Driven Contracts is an approach to ensure service communication compatibility, in which Consumer and Provider make an agreement about the format of the data they transfter between each other. This agreement forms the so called Contract. Normally, the format of the contract is defined by the Consumer and shared with the corresponding Provider. Afterwards, tests are being implemented in order to verify that the contract is being kept, called Contract Tests. CDC Testing requires both sides to be involved, thus one of the prerequisites for successful Contract Testing is the possibility to have a good, at best case close communication with the Provider service team (for example when you are the owner of the Consumer and Provider). Sharing those contracts and communicating the test results is important part of implementing proper CDC tests."
      )
      it should BeInvestedIn(
        "Consumer-Driven Contract Testing is a good idea to ensure API compatibility within an organisation."
      )
    }

  val go: Language = ea describes Language(title = "Go") as { it =>
    it has Description(
      "Go is a statically typed, compiled programming language designed at Google."
    )
    it has Description(
      "Go is syntactically similar to C, but with memory safety, garbage collection, structural typing, and CSP-style concurrency"
    )
    it should BeEliminated(
      "We are standardizing as much as possible on Typescript and NodeJS for backend development. Further more, Go developers are still rather hard to find, so investing on this technology would pose a risk."
    )
  }

  val dora: Technique = ea describes Technique(title = "DORA Metrics") as {
    it =>
      it has Description(
        "DORA metrics are a result of six years worth of surveys conducted by the DORA (DevOps Research and Assessments) team, that, among other data points, specifically measure deployment frequency (DF), mean lead time for changes (MLT), mean time to recover (MTTR) and change failure rate (CFR). These metrics serve as a guide to how well the engineering teams are performing and how successful a company is at DevOps, ranging from low performers to elite performers. They help answer the question: Are we better at DevOps now than we were a year ago? They help DevOps and engineering leaders measure software delivery throughput (velocity) and stability (quality). They show how development teams can deliver better software to their customers, faster. These metrics provide leaders with concrete data so they can gauge the organization’s DevOps performance—and so they can report to executives and recommend improvements. DORA metrics help align development goals with business goals. From a product management perspective, they offer a view into how and when development teams can meet customer needs. For engineering and DevOps leaders, these metrics can help prove that DevOps implementation has a clear business value."
      )
      it should BeInvestedIn()
      it isKnownBy actors.diana
  }

  val nodeJs: Framework = ea describes Framework(title = "NodeJS") as { it =>
    it has Description(
      "Node.js is an open-source, cross-platform, back-end JavaScript runtime environment that runs on the V8 engine and executes JavaScript code outside a web browser."
    )
    it has Description(
      "Node.js has an event-driven architecture capable of asynchronous I/O. These design choices aim to optimize throughput and scalability in web applications with many input/output operations, as well as for real-time Web applications."
    )
    it should BeInvestedIn(
      "Most of the recent NG services has been built on NodeJS. We have good knowledge of the technology."
    )
  }

  val javascript: Language = ea describes Language(title = "Javascript") as {
    it =>
      it has Description(
        "JavaScript is a high-level, often just-in-time compiled language that conforms to the ECMAScript standard."
      )
      it has Description(
        "It has dynamic typing, prototype-based object-orientation, and first-class functions. It is multi-paradigm, supporting event-driven, functional, and imperative programming styles."
      )
      it should BeMigrated(
        "Although Javascript is everywhere, it's design and especially its dynamic typing can be the cause of a lot of bugs. We advise to use Typescript, with all the added benefits of static type checking, as much as possible."
      )
  }

  val openTelemetry: Framework =
    ea describes Framework(title = "Open Telemetry") as { it =>
      it has Description(
        "OpenTelemetry is a set of APIs, SDKs, tooling and integrations that are designed for the creation and management of telemetry data such as traces, metrics, and logs. It is an open-source, vendor-neutral project, that is adopted and supported by industry leaders in the observability space."
      )
      it should BeInvestedIn()
      it isKnownBy actors.victorStone
      it isKnownBy (target = actors.barryAllen, level = Expert)
    }

  val kotlin: Language = ea describes Language(title = "Kotlin") as { it =>
    it has Description(
      "Kotlin is a cross-platform, statically typed, general-purpose programming language with type inference, mainly targeting the JVM."
    )
    it has Website("https://kotlinlang.org/")
    it should BeInvestedIn(
      "Kotlin is the preferred language for developing native Android apps."
    )
  }

}
