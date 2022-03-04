package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItPlatformSpec extends AnyFlatSpec with GivenWhenThen {
  "IT Platforms" can "be added to the town plan" in new EnterpriseArchitectureContext {
    When("a platform is added to the town plan")
    val thePlatform: ItPlatform =
      ea describes ItPlatform(title = "The Platform") as { it =>
        it has Description("a description")
        it should BeInvestedIn()
        it has Strength("It is there")
        it has Weakness("It is not perfect")
        it has Opportunity("But we can improve it")
        it has Threat("Unless we don't have time")
      }

    Then("the platform exists")
    assert(exists(thePlatform))
    And("it has the correct architecture verdict")
    assert(townPlan.platform(thePlatform.key).exists(_.isToBeInvestedIn))
    And("it has the correct SWOT analysis")
    assert(
      townPlan
        .platform(thePlatform.key)
        .exists(p =>
          p.swots.size == 4 && p.strengths.nonEmpty && p.weaknesses.nonEmpty && p.opportunities.nonEmpty && p.threats.nonEmpty
        )
    )
  }
}
