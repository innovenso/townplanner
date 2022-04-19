package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplanner.io.latex.test.LatexIO
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.views.{
  ArchitectureDecisionRecord,
  CompiledArchitectureDecisionRecord
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import tikz.txt.{
  DecisionOptionRequirementScoreSpiderDiagram,
  SecurityImpactDiagram
}

class DecisionSecurityImpactDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "A security impact diagram" should "be rendered for each decision" in new LatexIO {
    Given("a town plan with a decision")
    val enterprise: Enterprise = samples.enterprise
    samples.decision(Some(enterprise))
    When("an ADR is requested")
    val view: ArchitectureDecisionRecord = ea needs ArchitectureDecisionRecord()
    val compiledView: CompiledArchitectureDecisionRecord =
      townPlan.architectureDecisionRecord(view.key).get

    val latexSourceCode: String = SecurityImpactDiagram(
      townPlan,
      compiledView.decoratedDecisions.head.decision,
      "Test Decision"
    ).body

    println(latexSourceCode)

    assert(
      assetsExistWhen(
        pdfIsWritten(latexSourceCode)
      )
    )

  }
}
