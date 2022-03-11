package com.wayneenterprises.townplan.application

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.meta._
import com.wayneenterprises.townplan.business.Actors
import com.wayneenterprises.townplan.strategy._
import com.wayneenterprises.townplan.technology.Technologies

case class Systems()(implicit
    ea: EnterpriseArchitecture,
    enterprises: Enterprises,
    technologyRadar: Technologies,
    capabilities: BusinessCapabilities,
    buildingBlocks: BuildingBlocks,
    actors: Actors
) {
  val batCaveDestructionDay: Day = Day(2027, 10, 31)

  val bcms: ItSystem = {
    ea describes ItSystem(title = "BatCave Management System") as { it =>
      it has Description(
        "A custom Lair Management System, designed and built by Lucius Fox"
      )
      it realizes buildingBlocks.lairManagement
      it isUsedBy actors.bruceWayne
    }
  }

  val nemesisDb: Database =
    ea describes Database(title = "Nemesis Database") as { it =>
      it has Description(
        "A huge database with all the files of supervillains around the world"
      )
      it isImplementedBy technologyRadar.mongodb

      it has StartedDevelopment(description =
        "Ra's al Ghul came to town"
      ) on Day(2003, 1, 1)
      it has GoneToProduction(description =
        "Just in time for the Joker"
      ) on Day(2005, 1, 1)
      it is Decommissioned(description =
        "The Batcave is destroyed"
      ) on batCaveDestructionDay

      it isPartOf bcms
    }

  val nemesisMs: Microservice =
    ea describes Microservice(title = "Nemesis Microservice") as { it =>
      it has Description("The single point of truth for supervillains")

      it isImplementedBy technologyRadar.java
      it isImplementedBy technologyRadar.kubernetes

      it uses (target = nemesisDb, title = "fetches villain information")

      it isPartOf bcms

      it has StartedDevelopment(description =
        "Lucius Fox gets excited over microservices"
      ) on Day(2014, 1, 1)
      it has GoneToProduction(description =
        "Even Batman is not immune to microservices"
      ) on Day(2015, 1, 1)
      it is Decommissioned(description =
        "The Batcave is destroyed"
      ) on batCaveDestructionDay
    }

  val platformMs: Microservice = ea describes Microservice(title =
    "Batmobile Platform Rotation Microservice"
  ) as { it =>
    it has Description("Rotates the Batmobile platform")

    it isPartOf bcms

    it isImplementedBy technologyRadar.java
    it isImplementedBy technologyRadar.kubernetes

    it is Decommissioned(description =
      "The Batcave is destroyed"
    ) on batCaveDestructionDay
  }

  val bcmsUi: TerminalUI =
    ea describes TerminalUI(title = "Batcave Terminal") as { it =>
      it has Description(
        "Impressive user interface used to manage every aspect of the BatCave Management System"
      )
      it isImplementedBy technologyRadar.react

      it isPartOf bcms

      it uses nemesisMs
      it uses platformMs
      it isUsedBy actors.bruceWayne

      it has StartedDevelopment(description =
        "Lucius Fox gets excited over SPA"
      ) on Day(2017, 1, 1)
      it has GoneToProduction(description =
        "Even Batman is not immune to the SPA antipattern"
      ) on Day(2018, 1, 1)
      it is Decommissioned(description =
        "The Batcave is destroyed"
      ) on batCaveDestructionDay
    }

}
