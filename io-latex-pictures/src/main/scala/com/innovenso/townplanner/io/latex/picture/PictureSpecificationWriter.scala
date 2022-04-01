package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplanner.io.latex.model.{
  Book,
  KaoBookLibrary,
  LatexSpecification
}
import com.innovenso.townplanner.io.latex.picture.context.TikzPicture
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
          outputType = TikzPicture
        )
      )
    case architectureDecisionRecord: CompiledArchitectureDecisionRecord =>
      architectureDecisionRecord.decoratedDecisions
        .flatMap(_.options)
        .map(optionDecorator =>
          LatexSpecification(
            architectureDecisionRecord,
            latexSourceCode = DecisionOptionRequirementScoreSpiderDiagram(
              townPlan = townPlan,
              option = optionDecorator
            ).body,
            filenameAppendix = Some(
              optionDecorator.decision
                .map(decision => decision.title + " - ")
                .getOrElse("") + optionDecorator.option.title
            ),
            outputType = TikzPicture
          )
        )
    case _ => Nil
  }
}
