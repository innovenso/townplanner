package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.io.context.WebsiteThemeConfiguration
import com.innovenso.townplanner.model.concepts.Enterprise
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class WebsiteThemeSpec extends AnyFlatSpec with GivenWhenThen {
  "The theme of the website" should "be customizable" in new WebsiteIO {
    Given("an enterprise")
    val enterprise: Enterprise = samples.enterprise
    And("a custom theme")
    WebsiteThemeConfiguration.configureTheme(TestWebsiteTheme)
    When("the website is generated")
    websiteWriter.write()(townPlan, OutputContext(Nil))
    Then("the website exists")
    println(
      assetRepository.objectNames
        .flatMap(assetRepository.read)
        .map(_.getAbsolutePath)
    )

  }
}
