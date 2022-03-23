package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplanner.io.latex.model.{
  Book,
  KaoBookLibrary,
  LatexSpecification
}
import com.innovenso.townplanner.io.latex.picture.context.TikzPicture
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.CompiledBusinessCapabilityMap
import com.innovenso.townplanner.model.language.{CompiledView, View}
import tikz.txt.BusinessCapabilityMapPicture

object PictureSpecificationWriter {
  def specifications(
      townPlan: TownPlan,
      view: CompiledView[_ <: View]
  ): List[LatexSpecification] = view match {
    case businessCapabilityMap: CompiledBusinessCapabilityMap =>
      List(
        LatexSpecification(
          view = businessCapabilityMap,
          latexSourceCode = BusinessCapabilityMapPicture(
            businessCapabilityMap,
            BusinessCapabilityLayoutHelper(businessCapabilityMap)
          ).body,
          outputType = TikzPicture
        )
      )
    case _ => Nil
  }
}
