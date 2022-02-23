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

    And("some architecture building blocks")
    val buildingBlockUnderTest: ArchitectureBuildingBlock =
      ea describes ArchitectureBuildingBlock(title = "CI/CD Platform") as {
        it =>
          it realizes productDevelopment
      }

    val otherBuildingBlock: ArchitectureBuildingBlock =
      ea describes ArchitectureBuildingBlock(title = "UX Design tool") as {
        it =>
          it realizes productDesign
      }

    And("some platforms")
    val includedPlatform: ItPlatform =
      ea describes ItPlatform(title = "Included") as { it =>
        it realizes buildingBlockUnderTest
      }

    val excludedPlatform: ItPlatform =
      ea describes ItPlatform(title = "Excluded") as { it =>
        it realizes otherBuildingBlock
      }

    And("some systems")
    val includedPlatformSystem: ItSystem =
      ea describes ItSystem(title = "Included Platform System") as { it =>
        it isPartOf includedPlatform
      }

    val includedSystem: ItSystem =
      ea describes ItSystem(title = "Included System") as { it =>
        it realizes buildingBlockUnderTest
      }

    val excludedSystem: ItSystem = ea has ItSystem(title = "some other system")
    And("some containers")
    val includedContainer: ItContainer =
      ea describes Microservice(title = "microservice") as { it =>
        it isPartOf includedSystem
      }

    val excludedContainer: ItContainer =
      ea describes Microservice(title = "other microservice") as { it =>
        it isPartOf excludedSystem
      }

    When("an architecture building block realization view is requested")
    val viewUnderTest: ArchitectureBuildingBlockRealizationView =
      ea needs ArchitectureBuildingBlockRealizationView(forBuildingBlock =
        buildingBlockUnderTest.key
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
