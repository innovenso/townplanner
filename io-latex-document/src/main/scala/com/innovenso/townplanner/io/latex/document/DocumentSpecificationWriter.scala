package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplanner.io.latex.model.{
  Book,
  KaoBookLibrary,
  LatexSpecification
}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledArchitectureDecisionRecord,
  CompiledFullTownPlanView,
  CompiledTechnologyRadar
}
import com.innovenso.townplanner.model.language.{CompiledView, View}
import document.txt.{
  ArchitectureDecisionRecordDocument,
  FullTownPlan,
  TechnologyRadarDocument
}

object DocumentSpecificationWriter {
  def specifications(
      townPlan: TownPlan,
      view: CompiledView[_ <: View]
  ): List[LatexSpecification] = view match {
    case fullTownPlanView: CompiledFullTownPlanView =>
      List(
        LatexSpecification(
          view = fullTownPlanView,
          latexSourceCode = FullTownPlan(townPlan, fullTownPlanView).body,
          latexLibraries = List(KaoBookLibrary),
          outputType = Book
        )
      )
    case technologyRadar: CompiledTechnologyRadar =>
      List(
        LatexSpecification(
          view = technologyRadar,
          latexSourceCode = TechnologyRadarDocument(townPlan, technologyRadar).body,
          latexLibraries = List(KaoBookLibrary),
          outputType = Book
        )
      )
    case adr: CompiledArchitectureDecisionRecord =>
      List(
        LatexSpecification(
          view = adr,
          latexSourceCode = ArchitectureDecisionRecordDocument(townPlan, adr).body,
          latexLibraries = List(KaoBookLibrary),
          outputType = Book
        )
      )
    case _ => Nil
  }
}
