package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.io.latex.model.Book
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
    (1 to samples.randomInt(5)).foreach(_ => samples.decision(Some(enterprise)))
    When("an ADR is requested")
    val view: ArchitectureDecisionRecord = ea needs ArchitectureDecisionRecord()
    val pictureOutputContext: OutputContext = picturesAreWritten(view.key)
    println(assetRepository.targetBasePath)
    val outputContext: OutputContext =
      documentsAreWritten(view.key, pictureOutputContext)
    println(assetRepository.targetBasePath)
    Then("documents are available in the output context")
    assert(outputContext.outputs(ofOutputType = Some(Book)).nonEmpty)
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
