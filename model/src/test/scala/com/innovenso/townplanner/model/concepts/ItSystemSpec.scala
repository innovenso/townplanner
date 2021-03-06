package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  BeInvestedIn,
  Catastrophic,
  Description,
  ResilienceMeasure
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItSystemSpec extends AnyFlatSpec with GivenWhenThen {
  "IT Systems" can "be added to the town plan" in new EnterpriseArchitectureContext {
    Given("a platform")
    val platform: ItPlatform = ea has ItPlatform(title = "The Platform")
    When("a system is described")
    val itSystem: ItSystem = ea describes ItSystem(title = "The System") as {
      it =>
        it has Description("It does things")
        it should BeInvestedIn()
        it ratesFailureAs Catastrophic(consequences = "people die")
        it provides ResilienceMeasure("circuit breaker")
        it isPartOf platform
        it isIdentifiedAs "abc" on "Sparx"
    }

    Then("the system exists")
    assert(exists(itSystem))
    And("the system has the right relationships")
    assert(townPlan.relationships.size == 1)
    And("the system has the correct criticality")
    assert(townPlan.system(itSystem.key).get.isCatastrophicCriticality)
    And("the system has the correct external IDs")
    assert(townPlan.system(itSystem.key).exists(_.externalIds.nonEmpty))
  }

}
