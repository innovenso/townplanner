package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{
  CompiledFullTownPlanView,
  FullTownPlanView
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class FullTownPlanDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "Specifications and diagrams" should "be created" in new DiagramIO {
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

    And("some architecture building blocks")
    val buildingBlockUnderTest: ArchitectureBuildingBlock =
      samples.buildingBlock(realizedCapability = Some(productDevelopment))
    val otherBuildingBlock: ArchitectureBuildingBlock =
      samples.buildingBlock(realizedCapability = Some(productDesign))

    And("some platforms")
    val platform1: ItPlatform =
      samples.platform(realizedBuildingBlock = Some(buildingBlockUnderTest))
    val platform2: ItPlatform =
      samples.platform(realizedBuildingBlock = Some(otherBuildingBlock))

    And("some systems")
    val platformSystem: ItSystem = samples.system(
      withContainers = false,
      containingPlatform = Some(platform1)
    )
    val includedSystem: ItSystem =
      samples.system(realizedBuildingBlock = Some(buildingBlockUnderTest))
    val excludedSystem: ItSystem = samples.system(withContainers = true)

    When("a full town plan view is requested")
    val viewUnderTest: FullTownPlanView =
      ea needs FullTownPlanView(forEnterprise = innovenso.key)

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.fullTownPlanView(viewUnderTest.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(viewUnderTest.key))
  }
}
