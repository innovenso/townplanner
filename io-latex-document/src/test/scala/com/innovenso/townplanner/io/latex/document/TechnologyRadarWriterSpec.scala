package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.views.{
  FullTownPlanView,
  TechnologyRadar
}
import com.innovenso.townplanner.model.concepts.{
  ArchitectureBuildingBlock,
  BusinessCapability,
  Enterprise,
  ItSystem
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class TechnologyRadarWriterSpec extends AnyFlatSpec with GivenWhenThen {
  "A technology radar" should "result in a technology radar document" in new LatexDocumentIO {
    Given("a town plan with some elements")
    val enterprise: Enterprise = samples.enterprise
    val capability: BusinessCapability = samples.capability(Some(enterprise))
    val buildingBlock: ArchitectureBuildingBlock =
      samples.buildingBlock(Some(capability))
    val system: ItSystem =
      samples.system(realizedBuildingBlock = Some(buildingBlock))

    (1 to 20).foreach(it => {
      samples.technique
      samples.tool
      samples.language
      samples.framework
      samples.platformTechnology
    })

    And("a technology radar")
    val view: TechnologyRadar =
      ea needs TechnologyRadar(title = "Test Technology Radar")
    When("the townplan documents are written")
    val outputContext: OutputContext = documentsAreWritten(view.key)
    Then("documents are available in the output context")
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
