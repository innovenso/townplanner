package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlanFactory
import com.innovenso.townplanner.model.concepts.properties.{
  BeEliminated,
  Description
}
import com.innovenso.townplanner.model.concepts.relationships.{
  Composition,
  Flow
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import java.time.LocalDate

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

    ea hasRelationship Composition(
      source = innovenso.key,
      target = geniusfish.key,
      title = "hello world"
    )

    ea has KeyPointInTime(LocalDate.now(), "today")
    ea describes KeyPointInTime(LocalDate.of(2022, 7, 1), "pi8") as { it =>
      it has Description("the start of the summer")
    }

    println(ea.townPlan)
    assert(ea.townPlan.has(innovenso))
    assert(
      ea.townPlan.technology(java.key).isDefined && ea.townPlan
        .technology(java.key)
        .get
        .architectureVerdict
        .isInstanceOf[BeEliminated]
    )
    assert(ea.townPlan.pointsInTime.size == 2)
  }
}
