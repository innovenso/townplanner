package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  BeInvestedIn,
  Description
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItPlatformSpec extends AnyFlatSpec with GivenWhenThen {
  "IT Platforms" can "be added to the town plan" in new EnterpriseArchitecture {
    val thePlatform: ItPlatform =
      ea describes ItPlatform(title = "The Platform") as { it =>
        it has Description("a description")
        it should BeInvestedIn()
      }

    assert(exists(thePlatform))
    assert(townPlan.platform(thePlatform.key).exists(_.isToBeInvestedIn))
  }
}
