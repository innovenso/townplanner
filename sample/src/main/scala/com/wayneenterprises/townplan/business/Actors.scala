package com.wayneenterprises.townplan.business

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.concepts._
import com.wayneenterprises.townplan.strategy.Enterprises
import com.wayneenterprises.townplan.technology.Technologies

case class Actors()(implicit
    ea: EnterpriseArchitecture,
    enterprises: Enterprises
) {
  val robin: Actor = ea describes Actor(title = "Prospect Employee") as { he =>
    he serves enterprises.wayneCorp
  }

  val villain: Actor =
    ea describes Actor(title = "Supervillain") as { he =>
      he serves enterprises.wayneCorp
    }

  val justiceLeague: Team = ea describes Team(title = "Justice League") as {
    it =>
      it serves enterprises.wayneCorp
      it has Description("Superheroes fighting powerful enemies together")
      it has Wiki("https://nl.wikipedia.org/wiki/Justice_League_(film)")
  }

  val clarkKent: Person = ea describes Person(title = "Clark Ken") as { he =>
    he has Description("I'm Superman")
    he isPartOf justiceLeague
  }

  val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
    he has Description("I'm Batman")
    he isPartOf justiceLeague
    he serves enterprises.wayneCorp
    he has Description(
      "Bruce lost his parents at a young age, and has been a bit strange ever since."
    )
    he has Website("https://linkedin.com/brucewayne")
    he isIdentifiedAs "{42a5e9d4-188d-4509-8145-eaac65ab86b8}" on "Sparx EA"

    he has Strength("he has lots of gadgets")
    he has Strength("Alfred helps him a lot")
    he has Weakness("He is a bit traumatized and unstable")
    he has Threat(
      "He seems to have a conflict with Superman, which is not the best idea in the world"
    )
    he has Opportunity(
      "if he works together with the Justice League, he will be much stronger"
    )
  }
}
