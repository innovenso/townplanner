package com.innovenso.townplanner.io.openexchange

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.views.FullTownPlanView
import com.innovenso.townplanner.model.concepts.{
  ArchitectureBuildingBlock,
  BusinessCapability,
  Enterprise,
  ItPlatform,
  ItSystem,
  ItSystemIntegration
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class TownPlanOpenExchangeWriterSpec extends AnyFlatSpec with GivenWhenThen {
  "A full town plan" should "should contain all elements" in new OpenExchangeIO {
    val enterprise: Enterprise = samples.enterprise
    val cap: BusinessCapability = samples.capability(Some(enterprise))
    val cap2: BusinessCapability = samples.capability(Some(enterprise))
    val abb: ArchitectureBuildingBlock = samples.buildingBlock(Some(cap))
    val platform: ItPlatform = samples.platform(Some(abb))
    val system: ItSystem = samples.system(containingPlatform = Some(platform))
    val system2: ItSystem = samples.system()
    val integration: ItSystemIntegration = samples.integration(system, system2)
    samples.actor
    samples.teamMember(samples.team)
    samples.project(Some(enterprise))
    samples.principle(Some(enterprise))
    samples.decision(Some(enterprise))
    Then("the business capabilities are there")
    assert(townPlan.businessCapabilities.forall(xmlContainsElement))
    And("the building blocks are there")
    assert(
      townPlan.architectureBuildingBlocks.forall(xmlContainsElement)
    )
    And("the platforms are there")
    assert(
      townPlan.platforms.forall(xmlContainsElement)
    )
    And("the systems are there")
    assert(
      townPlan.systems.forall(xmlContainsElement)
    )
    And("the containers are there")
    assert(
      townPlan.containers.forall(xmlContainsElement)
    )
    And("the integrations are there")
    assert(
      townPlan.systemIntegrations.forall(xmlContainsElement)
    )

    And("the people are there")
    assert(
      townPlan.businessActors.forall(xmlContainsElement)
    )
    And("the projects are there")
    assert(
      townPlan.itProjects.forall(xmlContainsElement)
    )
    And("the milestones are there")
    assert(
      townPlan.itProjects
        .flatMap(townPlan.itProjectMilestones)
        .forall(xmlContainsElement)
    )
    And("the principles are there")
    assert(
      townPlan.principles
        .forall(xmlContainsElement)
    )
    And("the decisions are there")
    assert(
      townPlan.decisions
        .forall(xmlContainsElement)
    )
    And("the decision options are there")
    assert(
      townPlan.decisions
        .flatMap(townPlan.options)
        .forall(xmlContainsElement)
    )
    And("the technologies are there")
    assert(
      townPlan.technologies
        .forall(xmlContainsElement)
    )
    writeOpenExchange
  }

}
