package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplanner.io.latex.model.{
  Book,
  KaoBookLibrary,
  LatexSpecification
}
import com.innovenso.townplanner.io.latex.picture.context.{
  TikzBusinessCapabilityOnePager,
  TikzPicture,
  TikzRequirementScoreSpiderDiagram
}
import com.innovenso.townplanner.io.latex.picture.specifications.ArchitectureDecisionRecordSpecificationFactory
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledArchitectureDecisionRecord,
  CompiledBusinessCapabilityMap
}
import com.innovenso.townplanner.model.language.{CompiledView, View}
import tikz.txt.{
  BusinessCapabilityMapPicture,
  DecisionOptionRequirementScoreSpiderDiagram
}

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
            townPlan,
            businessCapabilityMap,
            BusinessCapabilityLayoutHelper(businessCapabilityMap)
          ).body,
          outputType = TikzBusinessCapabilityOnePager
        )
      )
    case architectureDecisionRecord: CompiledArchitectureDecisionRecord =>
      ArchitectureDecisionRecordSpecificationFactory(
        architectureDecisionRecord,
        townPlan
      ).specifications
    case _ => Nil
  }
}
