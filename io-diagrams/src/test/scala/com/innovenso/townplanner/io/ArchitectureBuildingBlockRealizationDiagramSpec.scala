package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{
  ArchitectureBuildingBlockRealizationView,
  CompiledArchitectureBuildingBlockRealizationView
}
import com.innovenso.townplanner.model.concepts._
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ArchitectureBuildingBlockRealizationDiagramSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "Architecture Building Block Realization Views" can "be added to the town plan" in new DiagramIO {
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
    val includedPlatform: ItPlatform =
      samples.platform(realizedBuildingBlock = Some(buildingBlockUnderTest))
    val excludedPlatform: ItPlatform =
      samples.platform(realizedBuildingBlock = Some(otherBuildingBlock))

    And("some systems")
    val includedPlatformSystem: ItSystem =
      samples.system(withContainers = false, Some(includedPlatform), None)
    val includedSystem: ItSystem =
      samples.system(withContainers = false, None, Some(buildingBlockUnderTest))
    val excludedSystem: ItSystem = samples.system(withContainers = false)
    And("some containers")
    val includedContainer: Microservice = samples.microservice(includedSystem)
    val excludedContainer: ItContainer = samples.microservice(excludedSystem)

    When("an architecture building block realization view is requested")
    val viewUnderTest: ArchitectureBuildingBlockRealizationView =
      ea needs ArchitectureBuildingBlockRealizationView(
        forBuildingBlock = buildingBlockUnderTest.key
      )

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.architectureBuildingBlockRealizationView(viewUnderTest.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(viewUnderTest.key))

  }
}
