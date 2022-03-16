package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplanner.io.latex.LatexPdfWriter
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.CompiledTechnologyRadar
import com.innovenso.townplanner.model.language.{CompiledView, View}

object DocumentPdfWriterFactory {
  def writers(
      townPlan: TownPlan,
      view: CompiledView[_ <: View]
  ): List[LatexPdfWriter] = view match {
    case technologyRadar: CompiledTechnologyRadar => Nil
    case _                                        => Nil
  }

}
