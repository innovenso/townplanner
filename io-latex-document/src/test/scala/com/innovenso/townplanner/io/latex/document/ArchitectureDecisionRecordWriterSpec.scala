package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.views.{
  ArchitectureDecisionRecord,
  CompiledArchitectureDecisionRecord
}
import com.innovenso.townplanner.model.concepts.{
  ArchitectureBuildingBlock,
  BusinessCapability,
  Decision,
  Enterprise,
  ItSystem
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ArchitectureDecisionRecordWriterSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "Decisions" should "appear in an architecture decision record document" in new LatexDocumentIO {
    Given("a town plan with a decision")
    val enterprise: Enterprise = samples.enterprise
    (1 to samples.randomInt(20)).foreach(_ =>
      samples.decision(Some(enterprise))
    )
    When("an ADR is requested")
    val view: ArchitectureDecisionRecord = ea needs ArchitectureDecisionRecord()
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