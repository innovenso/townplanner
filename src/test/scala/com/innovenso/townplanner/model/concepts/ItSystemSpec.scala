package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  BeInvestedIn,
  Description
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItSystemSpec extends AnyFlatSpec with GivenWhenThen {
  "IT Systems" can "be added to the town plan" in new EnterpriseArchitecture {
    val platform: ItPlatform = ea has ItPlatform(title = "The Platform")
    val itSystem: ItSystem = ea describes ItSystem(title = "The System") as {
      it =>
        it has Description("It does things")
        it should BeInvestedIn()
        it isPartOf platform
    }

    assert(exists(itSystem))
    assert(townPlan.relationships.size == 1)
  }

}