package com.innovenso.townplanner.io.latex.slides

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.{
  ArchitectureBuildingBlock,
  BusinessCapability,
  Enterprise,
  ItSystem
}
import com.innovenso.townplanner.model.concepts.views.FullTownPlanView
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class FullTownPlanSlideDeckSpec extends AnyFlatSpec with GivenWhenThen {
  "Full town plan slide deck" can "be rendered" in new LatexSlideDeckIO {
    Given("a town plan with some elements")
    val enterprise: Enterprise = samples.enterprise
    val capability: BusinessCapability = samples.capability(Some(enterprise))
    val buildingBlock: ArchitectureBuildingBlock =
      samples.buildingBlock(Some(capability))
    val system: ItSystem =
      samples.system(realizedBuildingBlock = Some(buildingBlock))
    And("a full town plan view")
    val view: FullTownPlanView =
      ea needs FullTownPlanView(forEnterprise = enterprise)
    When("the townplan slide decks are written")
    val outputContext: OutputContext = slideDecksAreWritten(view.key)
    Then("slide decks are available in the output context")
    assert(outputContext.outputs.size == 1)
    And("the files exist")
    assert(
      assetRepository.objectNames.forall(
        assetRepository
          .read(_)
          .exists(file => {
            println(file.getAbsolutePath)
            file.canRead
          })
      )
    )

  }
}
