package com.innovenso.townplanner.io.latex

import com.innovenso.townplan.io.context.{Book, Eps, Output, OutputContext}
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.views.FullTownPlanView
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import txt.Sample

class TownPlanLatexPdfWriterSpec extends AnyFlatSpec with GivenWhenThen {
  "a document" should "be rendered to PDF" in new LatexIO {
    Given("an enterprise")
    val enterprise: Enterprise = samples.enterprise
    And("a view")
    val townplanView: FullTownPlanView =
      ea needs FullTownPlanView(forEnterprise = enterprise)
    And("diagrams have been rendered")
    val diagramOutputContext: OutputContext =
      diagramsAreWritten(townplanView.key)

    When("a PDF is rendered")
    val sample: String =
      Sample(diagramOutputContext.outputsOfFileType(Eps).head, samples).body
    val output: List[Output] = LatexPdfWriter(
      sample,
      FullTownPlanView(forEnterprise = enterprise.key),
      Book,
      "sample document.pdf",
      assetRepository,
      diagramOutputContext
    ).document
    Then("the PDF exists")
    assert(output.nonEmpty)
  }
}
