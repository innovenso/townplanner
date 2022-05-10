package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.views.FullTownPlanView
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
    val fullTownPlanView: FullTownPlanView = ea needs FullTownPlanView(enterprise)
    val diagramOutputContext: OutputContext = diagramsAreWritten(fullTownPlanView.key)
    When("the website is written")
    websiteWriter.write()(townPlan, diagramOutputContext)
    Then("the website exists")
    println(assetRepository.objectNames.flatMap(assetRepository.read).map(_.getAbsolutePath))
  }
}
