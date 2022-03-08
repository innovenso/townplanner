package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{
  CompiledProjectMilestoneImpactView,
  ProjectMilestoneImpactView
}
import com.innovenso.townplanner.model.concepts._
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ProjectMilestoneImpactDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "a project milestone impact view" can "be rendered" in new DiagramIO {
    Given("an enterprise")
    val innovenso: Enterprise = samples.enterprise
    val apple: Enterprise = samples.enterprise

    And("some business capabilities")
    val marketing: BusinessCapability =
      samples.capability(servedEnterprise = Some(innovenso))
    val customerSegmentation: BusinessCapability =
      samples.capability(parentCapability = Some(marketing))

    And("some architecture building blocks")
    val buildingBlock1: ArchitectureBuildingBlock =
      samples.buildingBlock(realizedCapability = Some(customerSegmentation))
    val buildingBlock2: ArchitectureBuildingBlock =
      samples.buildingBlock(realizedCapability = Some(marketing))
    val system1: ItSystem =
      samples.system(realizedBuildingBlock = Some(buildingBlock1))
    val system2: ItSystem = samples.system()
    val system3: ItSystem = samples.system()

    And("some containers")
    val container1: Microservice = samples.microservice(system1)
    And("some integrations")
    val integration1: ItSystemIntegration =
      samples.integration(system1, system2)

    And("some technologies")
    val tech1: Technology = samples.language
    val tech2: Technology = samples.framework

    And("some platforms")
    val platform1: ItPlatform = samples.platform()
    val platform2: ItPlatform = samples.platform()

    And("a project milestone impacting them")
    val project: ItProject = ea describes ItProject(title = "the project") as {
      it =>
        it has Description("This project changes things")
    }

    val milestone: ItProjectMilestone =
      ea describes ItProjectMilestone(title = "milestone 1") as { it =>
        it isPartOf project
        it creates integration1
        it changes system1
        it keeps system2
        it removes customerSegmentation
        it creates buildingBlock1
        it removes buildingBlock2
        it creates platform1
        it changes platform2
        it creates tech1
        it removes tech2
        it changes container1
        it has Description("And this milestone changes some of them")
      }

    When("a project milestone impact view is requested")
    val viewUnderTest: ProjectMilestoneImpactView =
      ea needs ProjectMilestoneImpactView(forProjectMilestone = milestone.key)

    val compiledView: CompiledProjectMilestoneImpactView =
      townPlan.projectMilestoneImpactView(viewUnderTest.key).get

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.projectMilestoneImpactView(viewUnderTest.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(viewUnderTest.key))
  }
}
