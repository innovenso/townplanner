package com.innovenso.townplanner.io.latex

import com.innovenso.townplan.io.context.Book
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.views.FullTownPlanView
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import txt.Sample

class TownPlanLatexPdfWriterSpec extends AnyFlatSpec with GivenWhenThen {
  "a document" should "be rendered to PDF" in new LatexIO {
    Given("an enterprise")
    val enterprise: Enterprise = samples.enterprise
    Given("a sample LaTeX template")

    val sample: String = Sample(samples).body
    When("a PDF is rendered")
    val output = LatexPdfWriter.document(
      sample,
      FullTownPlanView(forEnterprise = enterprise.key),
      Book,
      "sample document.pdf",
      assetRepository
    )
    Then("the PDF exists")
    assert(output.nonEmpty)
  }
}
