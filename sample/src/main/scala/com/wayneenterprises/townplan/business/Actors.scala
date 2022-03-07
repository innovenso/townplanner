package com.wayneenterprises.townplan.business

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.properties.{Description, Opportunity, Strength, Threat, Weakness, Website, Wiki}
import com.innovenso.townplanner.model.concepts.{Actor, Person, Team}
import com.wayneenterprises.townplan.strategy.Enterprises

case class Actors()(implicit ea: EnterpriseArchitecture, enterprises: Enterprises) {
  val robin: Actor = ea describes Actor(title = "Prospect Employee") as { he =>
    he serves enterprises.wayneCorp
  }
  val justiceLeague: Team = ea describes Team(title = "Justice League") as { it =>
    it serves enterprises.wayneCorp
    it has Description("Superheroes fighting powerful enemies together")
    it has Wiki("https://nl.wikipedia.org/wiki/Justice_League_(film)")
  }
  val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
    he has Description("I'm Batman")
    he isPartOf justiceLeague
    he serves enterprises.wayneCorp
    he has Description("Bruce lost his parents at a young age, and has been a bit strange ever since.")
    he has Website("https://linkedin.com/brucewayne")

    he has Strength("he has lots of gadgets")
    he has Strength("Alfred helps him a lot")
    he has Weakness("He is a bit traumatized and unstable")
    he has Threat("He seems to have a conflict with Superman, which is not the best idea in the world")
    he has Opportunity("if he works together with the Justice League, he will be much stronger")
  }
}
