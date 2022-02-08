package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.BeEliminated
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class TechnologySpec extends AnyFlatSpec with GivenWhenThen {
  "Technologies" can "be added to the town plan" in new EnterpriseArchitecture {
    val java: Technique =
      ea describes Technique(title = "SAFE") as { it =>
        it should BeEliminated("SAFE is not agile")
      }

    assert(exists(java))
    assert(
      townPlan
        .component(java.key, classOf[Technique])
        .exists(_.isToBeEliminated)
    )
  }

}
