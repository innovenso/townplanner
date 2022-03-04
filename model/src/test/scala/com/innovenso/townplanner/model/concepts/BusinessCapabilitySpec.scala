package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Description
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class BusinessCapabilitySpec extends AnyFlatSpec with GivenWhenThen {
  "Business Capabilities" can "be added to the town plan" in new EnterpriseArchitectureContext {
    val innovenso: Enterprise = ea has Enterprise(title = "Innovenso")

    val marketing: BusinessCapability =
      ea describes BusinessCapability(title = "Marketing") as { it =>
        it has Description(
          "One of the most important capabilities of a modern enterprise"
        )
        it serves innovenso
      }

    val customerSegmentation: BusinessCapability =
      ea describes BusinessCapability(title = "Customer Segmentation") as {
        it =>
          it serves marketing
      }

    assert(exists(innovenso))
    assert(exists(marketing))
    assert(exists(customerSegmentation))
    assert(townPlan.relationships.size == 2)
    assert(townPlan.businessCapabilityMap(innovenso).size == 2)
  }

}
