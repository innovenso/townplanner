package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.properties.{Decommissioned, GoneToProduction, Retired, StartedDevelopment}
import com.innovenso.townplanner.model.concepts.relationships.Expert
import com.innovenso.townplanner.model.concepts.{ArchitectureBuildingBlock, BusinessCapability, Enterprise, ItPlatform, ItSystem, Person, Tag, Team}
import com.innovenso.townplanner.model.concepts.views.{ArchitectureBuildingBlockRealizationView, BusinessCapabilityMap, BusinessCapabilityPosition, FullTownPlanView, SystemContainerView}
import com.innovenso.townplanner.model.meta.{Day, InTheFuture, InThePast}
import org.scalatest.{GivenWhenThen, fullstacks}
import org.scalatest.flatspec.AnyFlatSpec

import java.io.File

class TownPlanWebsiteWriterSpec extends AnyFlatSpec with GivenWhenThen {
  "The working directory " should "be created" in new WebsiteIO {
    assert(websiteWriter.workingDirectory.exists(_.isDirectory))
  }

  "Documentation website" should "be written" in new WebsiteIO {
    Given("an enterprise")
    val enterprise: Enterprise = samples.enterprise
    val tag1: Tag = samples.tag
    val tag2: Tag = samples.tag
    val cap1: BusinessCapability = samples.capability(servedEnterprise = Some(enterprise), tags = List(tag1, tag2))
    val cap11: BusinessCapability = samples.capability(parentCapability = Some(cap1))
    val cap2: BusinessCapability = samples.capability(servedEnterprise = Some(enterprise), tags = List(tag2))
    val bb1: ArchitectureBuildingBlock = samples.buildingBlock(Some(cap1))
    val platform: ItPlatform = samples.platform(Some(bb1))
    val system1: ItSystem = samples.system(realizedBuildingBlock = Some(bb1), containingPlatform = Some(platform))
    val system2: ItSystem = ea describes ItSystem(title = samples.title) as { it =>
      it uses system1
      it uses townPlan.containers(system1).head
      it has StartedDevelopment() on Day(2021,10,1)
      it has GoneToProduction() on Day(2021,11,1)
      it is Retired() on Day(2023,1,1)
      it is Decommissioned() on Day(2023, 11,1)
    }
    val team1: Team = samples.team
    val person: Person = samples.teamMember(team1)
    townPlan.technologies.foreach(tech => samples.knowledge(person, tech, Expert))
    ea needs BusinessCapabilityPosition(cap1)
    ea needs FullTownPlanView(enterprise)
    ea needs BusinessCapabilityMap(enterprise)
    ea needs ArchitectureBuildingBlockRealizationView(bb1)
    ea needs SystemContainerView(system1)
    ea needs SystemContainerView(system1, InThePast)
    ea needs SystemContainerView(system1, InTheFuture)
    ea needs SystemContainerView(system1, Day(2023,7,1))
    ea needs SystemContainerView(system2)
    val diagramOutputContext: OutputContext = diagramsAreWritten
    When("the website is written")
    websiteWriter.write()(townPlan, diagramOutputContext)
    Then("the website exists")
    println(assetRepository.objectNames.flatMap(assetRepository.read).map(_.getAbsolutePath))
  }
}
