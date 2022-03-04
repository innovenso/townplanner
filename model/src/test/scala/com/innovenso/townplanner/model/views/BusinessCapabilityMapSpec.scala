package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{
  BusinessCapabilityMap,
  CompiledBusinessCapabilityMap
}
import com.innovenso.townplanner.model.concepts.{
  BusinessCapability,
  Enterprise,
  EnterpriseArchitectureContext
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class BusinessCapabilityMapSpec extends AnyFlatSpec with GivenWhenThen {
  "Business capability maps" can "be added to the town plan" in new EnterpriseArchitectureContext {
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

    Then("the business capability map exists")
    assert(exists(businessCapabilityMap))

    val compiledBusinessCapabilityMap: CompiledBusinessCapabilityMap =
      townPlan.businessCapabilityMap(businessCapabilityMap.key).get
    And("it contains the enterprise")
    compiledBusinessCapabilityMap.enterprises.contains(innovenso)
    !compiledBusinessCapabilityMap.enterprises.contains(apple)
    compiledBusinessCapabilityMap
      .level0businessCapabilities(innovenso)
      .contains(marketing)
    compiledBusinessCapabilityMap
      .level0businessCapabilities(innovenso)
      .contains(product)
    compiledBusinessCapabilityMap.childBusinessCapabilities(product).size == 2
    !compiledBusinessCapabilityMap.businessCapabilities.contains(
      otherEnterpriseCapability
    )

  }
}
