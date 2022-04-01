package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.io.latex.test.LatexIO
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.views.{
  ArchitectureDecisionRecord,
  CompiledArchitectureDecisionRecord
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import tikz.txt.{
  BusinessCapabilityMapPicture,
  DecisionOptionRequirementScoreSpiderDiagram
}

class DecisionOptionRequirementScoreSpiderDiagramSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "A spider diagram" should "be rendered for decision options with scores" in new LatexIO {
    Given("a town plan with a decision")
    val enterprise: Enterprise = samples.enterprise
    (1 to samples.randomInt(20)).foreach(_ =>
      samples.decision(Some(enterprise))
    )
    When("an ADR is requested")
    val view: ArchitectureDecisionRecord = ea needs ArchitectureDecisionRecord()
    val compiledView: CompiledArchitectureDecisionRecord =
      townPlan.architectureDecisionRecord(view.key).get

    val latexSourceCode: String = DecisionOptionRequirementScoreSpiderDiagram(
      townPlan,
      compiledView.decoratedDecisions.head.options.head
    ).body

    println(latexSourceCode)

    assert(
      assetsExistWhen(
        pdfIsWritten(latexSourceCode)
      )
    )

  }
}
