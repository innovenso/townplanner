package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.BeEliminated
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class TechnologySpec extends AnyFlatSpec with GivenWhenThen {
  "Technologies" can "be added to the town plan" in new EnterpriseArchitectureContext {
    When("a technology is added to the town plan")
    val java: Technique =
      ea describes Technique(title = "SAFE") as { it =>
        it should BeEliminated("SAFE is not agile")
      }

    Then("the technology exists")
    assert(exists(java))
    And("it has the correct architecture verdict")
    assert(
      townPlan
        .component(java.key, classOf[Technique])
        .exists(_.isToBeEliminated)
    )
  }

}
