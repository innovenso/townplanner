package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.BusinessCapabilityPosition
import com.innovenso.townplanner.model.concepts.{BusinessCapability, Enterprise}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class BusinessCapabilityPositionDiagramSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "Specifications and diagrams" should "be created for business capability positioins" in new DiagramIO {
    Given("an enterprise")
    val innovenso: Enterprise = samples.enterprise
    val apple: Enterprise = samples.enterprise

    And("some business capabilities")
    val marketing: BusinessCapability =
      samples.capability(servedEnterprise = Some(innovenso))
    val customerSegmentation: BusinessCapability =
      samples.capability(parentCapability = Some(marketing))
    val product: BusinessCapability =
      samples.capability(servedEnterprise = Some(innovenso))
    val productDesign: BusinessCapability =
      samples.capability(parentCapability = Some(product))
    val productDevelopment: BusinessCapability =
      samples.capability(parentCapability = Some(product))
    val otherEnterpriseCapability: BusinessCapability = samples.capability()

    When("a business capability position is requested")
    val businessCapabilityPosition: BusinessCapabilityPosition =
      ea needs BusinessCapabilityPosition(forCapability =
        productDevelopment.key
      )

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.businessCapabilityPosition(businessCapabilityPosition.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(businessCapabilityPosition.key))

  }
}
