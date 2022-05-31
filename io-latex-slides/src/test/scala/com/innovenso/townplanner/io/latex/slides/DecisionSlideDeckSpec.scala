package com.innovenso.townplanner.io.latex.slides

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.views.ArchitectureDecisionRecord
import com.innovenso.townplanner.model.concepts.{
  Decided,
  Decision,
  Enterprise,
  InProgress,
  NotStarted
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class DecisionSlideDeckSpec extends AnyFlatSpec with GivenWhenThen {
  "each decision" should "have its own slide deck" in new LatexSlideDeckIO {
    Given("a number of decisions")
    val innovenso: Enterprise = samples.enterprise
    val notStartedDecision: Decision =
      samples.decision(Some(innovenso), status = NotStarted)
//    val inProgressDecision: Decision =
//      samples.decision(Some(innovenso), status = InProgress)
//    val decidedDecision: Decision =
//      samples.decision(Some(innovenso), status = Decided)
    When("An ADR is requested")
    val adr: ArchitectureDecisionRecord = ea needs ArchitectureDecisionRecord()
    When("the townplan slide decks are written")
    val pictureOutputContext: OutputContext = picturesAreWritten(adr.key)
    println(assetRepository.targetBasePath)
    val outputContext: OutputContext =
      slideDecksAreWritten(adr.key, pictureOutputContext)
    Then("slide decks are available in the output context")
    assert(outputContext.outputs.nonEmpty)
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
