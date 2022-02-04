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

import scala.language.postfixOps

class EnterpriseSpec extends AnyFlatSpec with GivenWhenThen {
  "Enterprises" can "be added to the townplan" in {
    val architecture = new TownPlanFactory

    val innovenso =
      architecture describes Enterprise(title = Title("Innovenso")) as { it =>
        it has Documentation(description = Description(Some("hello")))
      }
    println(innovenso)
    val geniusfish =
      architecture has Enterprise(title = Title("Genius Fish"))

    val innovensogroup =
      architecture describes Enterprise(title = Title("Innovenso Group")) as {
        it =>
          it triggers geniusfish
      }

    architecture hasRelationship Relationship(
      source = innovenso.key,
      target = geniusfish.key,
      relationshipType = Triggers,
      title = Title("hello world")
    )

    println(geniusfish)
    println(innovensogroup)
    architecture.townPlan.relationships.foreach(r => println(r))

  }
}
