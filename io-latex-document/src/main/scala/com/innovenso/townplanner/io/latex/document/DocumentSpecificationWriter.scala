package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplanner.io.latex.model.{Book, LatexSpecification}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.CompiledFullTownPlanView
import com.innovenso.townplanner.model.language.{CompiledView, View}
import com.innovenso.townplanner.model.samples.SampleFactory
import document.txt.Sample

object DocumentSpecificationWriter {
  def specifications(
      townPlan: TownPlan,
      view: CompiledView[_ <: View]
  ): List[LatexSpecification] = view match {
    case fullTownPlanView: CompiledFullTownPlanView =>
      List(
        LatexSpecification(
          view = fullTownPlanView,
          latexSourceCode = Sample(fullTownPlanView).body,
          outputType = Book
        )
      )
    case _ => Nil
  }
}
