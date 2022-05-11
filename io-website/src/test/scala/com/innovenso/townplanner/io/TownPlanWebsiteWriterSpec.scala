package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.{BusinessCapability, Enterprise}
import com.innovenso.townplanner.model.concepts.views.{BusinessCapabilityMap, BusinessCapabilityPosition, FullTownPlanView}
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
    val cap2: BusinessCapability = samples.capability(Some(enterprise))
    ea needs BusinessCapabilityPosition(cap1)
    ea needs FullTownPlanView(enterprise)
    ea needs BusinessCapabilityMap(enterprise)
    val diagramOutputContext: OutputContext = diagramsAreWritten
    When("the website is written")
    websiteWriter.write()(townPlan, diagramOutputContext)
    Then("the website exists")
    println(assetRepository.objectNames.flatMap(assetRepository.read).map(_.getAbsolutePath))
  }
}
