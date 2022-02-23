package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{
  BusinessCapabilityPosition,
  CompiledBusinessCapabilityPosition
}
import com.innovenso.townplanner.model.concepts.{
  BusinessCapability,
  Enterprise,
  EnterpriseArchitecture
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class BusinessCapabilityPositionSpec extends AnyFlatSpec with GivenWhenThen {
  "Business capability position" can "be added to the town plan" in new EnterpriseArchitecture {
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

    val productDelivery: BusinessCapability =
      ea describes BusinessCapability(title = "Delivery") as { it =>
        it serves productDevelopment
      }

    val productDeployment: BusinessCapability =
      ea describes BusinessCapability(title = "Deployment") as { it =>
        it serves productDevelopment
      }

    val otherEnterpriseCapability: BusinessCapability =
      ea has BusinessCapability(title = "something else")

    When("a business capability position is requested")
    val businessCapabilityPosition: BusinessCapabilityPosition =
      ea needs BusinessCapabilityPosition(forCapability =
        productDevelopment.key
      )

    Then("the business capability map exists")
    assert(exists(businessCapabilityPosition))

    val compiledBusinessCapabilityPosition: CompiledBusinessCapabilityPosition =
      townPlan.businessCapabilityPosition(businessCapabilityPosition.key).get
    And("it contains the enterprise")
    compiledBusinessCapabilityPosition.enterprises.contains(innovenso)
    !compiledBusinessCapabilityPosition.enterprises.contains(apple)
    !compiledBusinessCapabilityPosition
      .level0businessCapabilities(innovenso)
      .contains(marketing)
    compiledBusinessCapabilityPosition
      .level0businessCapabilities(innovenso)
      .contains(product)
    compiledBusinessCapabilityPosition
      .childBusinessCapabilities(product)
      .size == 1
    !compiledBusinessCapabilityPosition.businessCapabilities.contains(
      otherEnterpriseCapability
    )
    !compiledBusinessCapabilityPosition.businessCapabilities.contains(
      productDesign
    )
    compiledBusinessCapabilityPosition.businessCapabilities.contains(
      productDelivery
    )
    compiledBusinessCapabilityPosition.businessCapabilities.contains(
      productDeployment
    )

  }
}
