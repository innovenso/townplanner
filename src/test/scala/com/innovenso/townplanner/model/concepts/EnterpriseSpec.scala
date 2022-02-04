package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlanFactory
import com.innovenso.townplanner.model.concepts.properties.{
  ArchitectureVerdict,
  Documentation,
  Tolerate
}
import com.innovenso.townplanner.model.meta.{Description, Title}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class EnterpriseSpec extends AnyFlatSpec with GivenWhenThen {
  "Enterprises" can "be added to the townplan" in {
    val ea = new TownPlanFactory

    val innovenso =
      ea describes Enterprise(title = Title("Innovenso")) as { it =>
        it has Documentation(description = Description(Some("hello")))
      }

    val geniusfish =
      ea has Enterprise(title = Title("Genius Fish"))

    val innovensogroup =
      ea describes Enterprise(title = Title("Innovenso Group")) as { it =>
        it triggers geniusfish
      }

    ea hasRelationship Relationship(
      source = innovenso.key,
      target = geniusfish.key,
      relationshipType = Triggers,
      title = Title("hello world")
    )
  }
}
