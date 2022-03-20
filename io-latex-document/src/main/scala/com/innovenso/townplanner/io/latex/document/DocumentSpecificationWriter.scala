package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplanner.io.latex.model.{
  Book,
  KaoBookLibrary,
  LatexSpecification
}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledFullTownPlanView,
  CompiledTechnologyRadar
}
import com.innovenso.townplanner.model.language.{CompiledView, View}
import document.txt.{FullTownPlan, TechnologyRadarDocument}

object DocumentSpecificationWriter {
  def specifications(
      townPlan: TownPlan,
      view: CompiledView[_ <: View]
  ): List[LatexSpecification] = view match {
    case fullTownPlanView: CompiledFullTownPlanView =>
      List(
        LatexSpecification(
          view = fullTownPlanView,
          latexSourceCode = FullTownPlan(fullTownPlanView).body,
          latexLibraries = List(KaoBookLibrary),
          outputType = Book
        )
      )
    case technologyRadar: CompiledTechnologyRadar =>
      List(
        LatexSpecification(
          view = technologyRadar,
          latexSourceCode = TechnologyRadarDocument(technologyRadar).body,
          latexLibraries = List(KaoBookLibrary),
          outputType = Book
        )
      )
    case _ => Nil
  }
}
