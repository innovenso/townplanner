package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlanFactory
import com.innovenso.townplanner.model.concepts.properties.{
  ArchitectureVerdict,
  BeEliminated,
  BeTolerated,
  Description
}
import com.innovenso.townplanner.model.concepts.relationships.Flow
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class EnterpriseSpec extends AnyFlatSpec with GivenWhenThen {
  "Enterprises" can "be added to the townplan" in {
    val ea = new TownPlanFactory

    val innovenso =
      ea describes Enterprise(title = "Innovenso") as { it =>
        it has Description("hello")
      }

    val geniusfish =
      ea has Enterprise(title = "genius fish")

    val innovensogroup =
      ea describes Enterprise(title = "Innovenso Group") as { it =>
        it isComposedOf geniusfish
      }

    val java =
      ea describes Technique(title = "SAFE") as { it =>
        it should BeEliminated("SAFE is not agile")
      }

    ea hasRelationship Flow(
      source = innovenso.key,
      target = geniusfish.key,
      title = "hello world"
    )
  }
}
