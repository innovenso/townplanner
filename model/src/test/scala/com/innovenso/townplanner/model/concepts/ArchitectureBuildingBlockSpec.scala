package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  BeInvestedIn,
  Description
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ArchitectureBuildingBlockSpec extends AnyFlatSpec with GivenWhenThen {
  "Architecture Building Blocks" can "be added to the town plan" in new EnterpriseArchitectureContext {
    val innovenso: Enterprise = ea has Enterprise(title = "Innovenso")
    val marketing: BusinessCapability =
      ea describes BusinessCapability(title = "Marketing") as { it =>
        it serves innovenso
      }
    val cdp: ArchitectureBuildingBlock =
      ea describes ArchitectureBuildingBlock(title = "CDP") as { it =>
        it should BeInvestedIn()
        it has Description("Customer Data Platform")
        it has Description(
          "a collection of software which creates a persistent, unified customer database that is accessible to other systems."
        )
        it has Description("notable vendors are Adobe, SAS, Tealium")
        it realizes marketing
      }

    assert(exists(cdp))
    assert(
      townPlan
        .architectureBuildingBlock(cdp.key)
        .exists(it => {
          it.descriptions.size == 3 && it.isToBeInvestedIn
        })
    )
  }

}
