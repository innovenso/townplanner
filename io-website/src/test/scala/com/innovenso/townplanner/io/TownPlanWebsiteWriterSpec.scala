package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.{ArchitectureBuildingBlock, BusinessCapability, Enterprise, ItSystem}
import com.innovenso.townplanner.model.concepts.views.{ArchitectureBuildingBlockRealizationView, BusinessCapabilityMap, BusinessCapabilityPosition, FullTownPlanView, SystemContainerView}
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
    val cap1: BusinessCapability = samples.capability(Some(enterprise))
    val cap11: BusinessCapability = samples.capability(parentCapability = Some(cap1))
    val cap2: BusinessCapability = samples.capability(Some(enterprise))
    val bb1: ArchitectureBuildingBlock = samples.buildingBlock(Some(cap1))
    val system1: ItSystem = samples.system(realizedBuildingBlock = Some(bb1))
    ea needs BusinessCapabilityPosition(cap1)
    ea needs FullTownPlanView(enterprise)
    ea needs BusinessCapabilityMap(enterprise)
    ea needs ArchitectureBuildingBlockRealizationView(bb1)
    ea needs SystemContainerView(system1)
    val diagramOutputContext: OutputContext = diagramsAreWritten
    When("the website is written")
    websiteWriter.write()(townPlan, diagramOutputContext)
    Then("the website exists")
    println(assetRepository.objectNames.flatMap(assetRepository.read).map(_.getAbsolutePath))
  }
}
