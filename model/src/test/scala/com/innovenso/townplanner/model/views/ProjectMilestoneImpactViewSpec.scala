package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{CompiledProjectMilestoneImpactView, ProjectMilestoneImpactView}
import com.innovenso.townplanner.model.concepts.{ArchitectureBuildingBlock, BusinessCapability, Enterprise, EnterpriseArchitecture, ItContainer, ItPlatform, ItProject, ItProjectMilestone, ItSystem, ItSystemIntegration, Microservice}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ProjectMilestoneImpactViewSpec extends AnyFlatSpec with GivenWhenThen {
  "a project milestone impact view" can "be added to the town plan" in new EnterpriseArchitecture {
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

    And("some architecture building blocks")
    val buildingBlock1: ArchitectureBuildingBlock =
      ea describes ArchitectureBuildingBlock(title = "CDP") as {
        it =>
          it realizes customerSegmentation
      }

    val buildingBlock2: ArchitectureBuildingBlock =
      ea describes ArchitectureBuildingBlock(title = "CRM") as {
        it =>
          it realizes marketing
      }

    val system1: ItSystem =
      ea describes ItSystem(title = "Included System") as { it =>
        it realizes buildingBlock1
      }

    val system2: ItSystem = ea has ItSystem(title = "some other system")

    val system3: ItSystem = ea has ItSystem(title = "a third system")

    And("some containers")
    val container1: ItContainer =
      ea describes Microservice(title = "microservice") as { it =>
        it isPartOf system1
      }

    val container2: ItContainer =
      ea describes Microservice(title = "other microservice") as { it =>
        it isPartOf system2
      }

    And("some integrations")
    val integration1: ItSystemIntegration = ea describes ItSystemIntegration(title = "An Integration") between system1 and system2 as { it =>
      it has Description("an integration")
    }

    And("a project milestone impacting them")
    val project: ItProject = ea has ItProject(title = "the project")

    val milestone: ItProjectMilestone = ea describes ItProjectMilestone(title = "milestone 1") as { it =>
      it isPartOf project
      it creates integration1
      it changes system1
      it keeps system2
      it removes customerSegmentation
      it creates buildingBlock1
      it removes buildingBlock2
    }

    When("a project milestone impact view is requested")
    val viewUnderTest: ProjectMilestoneImpactView = ea needs ProjectMilestoneImpactView(forProjectMilestone = milestone.key)

    val compiledView: CompiledProjectMilestoneImpactView = townPlan.projectMilestoneImpactView(viewUnderTest.key).get

    Then("all impacted elements are in the view")
    compiledView.systems.contains(system1)

  }
}
