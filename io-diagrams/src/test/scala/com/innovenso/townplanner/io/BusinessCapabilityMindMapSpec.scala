package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.BusinessCapabilityMap
import com.innovenso.townplanner.model.concepts.{BusinessCapability, Enterprise}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class BusinessCapabilityMindMapSpec extends AnyFlatSpec with GivenWhenThen {
  "Specifications and diagrams are created" can "for business capability maps" in new DiagramIO {
    Given("an enterprise")
    val innovenso: Enterprise = ea has Enterprise(title = "Innovenso")
    val apple: Enterprise = ea has Enterprise(title = "Apple")

    And("some business capabilities")
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

    val product: BusinessCapability =
      ea describes BusinessCapability(title = "Product") as { it =>
        it serves innovenso
      }

    val productDesign: BusinessCapability =
      ea describes BusinessCapability(title = "Product Design") as { it =>
        it serves product
      }

    val productDevelopment: BusinessCapability =
      ea describes BusinessCapability(title = "Product Development") as { it =>
        it serves product
      }

    val otherEnterpriseCapability: BusinessCapability =
      ea has BusinessCapability(title = "something else")

    When("a business capability map is requested")
    val businessCapabilityMap: BusinessCapabilityMap =
      ea needs BusinessCapabilityMap(forEnterprise = innovenso.key)

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.businessCapabilityMap(businessCapabilityMap.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(businessCapabilityMap.key))

  }
}
