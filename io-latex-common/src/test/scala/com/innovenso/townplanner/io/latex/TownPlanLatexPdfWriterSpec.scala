package com.innovenso.townplanner.io.latex

import com.innovenso.townplan.io.context.{Eps, Output, OutputContext}
import com.innovenso.townplanner.io.latex.model.{
  Book,
  KaoBookLibrary,
  LatexSpecification
}
import com.innovenso.townplanner.io.latex.test.LatexIO
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.views.FullTownPlanView
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import txt.Sample

class TownPlanLatexPdfWriterSpec extends AnyFlatSpec with GivenWhenThen {
  "a document with a custom LaTeX template" should "be rendered to PDF" in new LatexIO {
    Given("an enterprise")
    val enterprise: Enterprise = samples.enterprise
    And("a view")
    val townplanView: FullTownPlanView =
      ea needs FullTownPlanView(forEnterprise = enterprise)
    And("diagrams have been rendered")
    val diagramOutputContext: OutputContext =
      diagramsAreWritten(townplanView.key)

    When("a LaTeX specification is created")
    val sample: String =
      Sample(
        diagramOutputContext.outputsOfFileType(Eps).head,
        samples
      ).body
    val specification: LatexSpecification = LatexSpecification(
      view = townPlan.fullTownPlanView(townplanView.key).get,
      latexSourceCode = sample,
      latexLibraries = List(KaoBookLibrary),
      dependencies = diagramOutputContext.outputsOfFileType(Eps),
      filenameAppendix = None,
      outputType = Book
    )
    And("a PDF is rendered")
    val output: List[Output] = LatexPdfWriter(
      specification,
      assetRepository,
      diagramOutputContext
    ).document

  }
}
