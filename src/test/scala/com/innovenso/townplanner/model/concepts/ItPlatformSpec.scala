package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  BeInvestedIn,
  Description,
  Opportunity,
  Strength,
  Threat,
  Weakness
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItPlatformSpec extends AnyFlatSpec with GivenWhenThen {
  "IT Platforms" can "be added to the town plan" in new EnterpriseArchitecture {
    val thePlatform: ItPlatform =
      ea describes ItPlatform(title = "The Platform") as { it =>
        it has Description("a description")
        it should BeInvestedIn()
        it has Strength("It is there")
        it has Weakness("It is not perfect")
        it has Opportunity("But we can improve it")
        it has Threat("Unless we don't have time")
      }

    assert(exists(thePlatform))
    assert(townPlan.platform(thePlatform.key).exists(_.isToBeInvestedIn))
    assert(
      townPlan
        .platform(thePlatform.key)
        .exists(p =>
          p.swots.size == 4 && p.strengths.nonEmpty && p.weaknesses.nonEmpty && p.opportunities.nonEmpty && p.threats.nonEmpty
        )
    )
  }
}
