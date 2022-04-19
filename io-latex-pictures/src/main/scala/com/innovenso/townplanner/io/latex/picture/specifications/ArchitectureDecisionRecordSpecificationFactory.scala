package com.innovenso.townplanner.io.latex.picture.specifications

import com.innovenso.townplanner.io.latex.model.LatexSpecification
import com.innovenso.townplanner.io.latex.picture.context.{
  TikzRequirementScoreSpiderDiagram,
  TikzSecurityImpactDiagram
}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.CompiledArchitectureDecisionRecord
import tikz.txt.{
  DecisionOptionRequirementScoreSpiderDiagram,
  SecurityImpactDiagram
}

case class ArchitectureDecisionRecordSpecificationFactory(
    architectureDecisionRecord: CompiledArchitectureDecisionRecord,
    townPlan: TownPlan
) {
  val securityImpactDiagrams: List[LatexSpecification] =
    architectureDecisionRecord.decoratedDecisions.map(decisionDecorator =>
      LatexSpecification(
        view = architectureDecisionRecord,
        relatedModelComponents = List(decisionDecorator.decision),
        latexSourceCode = SecurityImpactDiagram(
          townPlan,
          decisionDecorator.decision,
          decisionDecorator.decision.title
        ).body,
        filenameAppendix = Some(decisionDecorator.decision.title),
        outputType = TikzSecurityImpactDiagram
      )
    )

  val scoreSpiderDiagrams: List[LatexSpecification] =
    architectureDecisionRecord.decoratedDecisions
      .flatMap(_.options)
      .map(optionDecorator =>
        LatexSpecification(
          view = architectureDecisionRecord,
          relatedModelComponents =
            List(optionDecorator.option) ::: optionDecorator.decision.toList,
          latexSourceCode = DecisionOptionRequirementScoreSpiderDiagram(
            townPlan = townPlan,
            option = optionDecorator
          ).body,
          filenameAppendix = Some(
            optionDecorator.decision
              .map(decision => decision.title + " - ")
              .getOrElse("") + optionDecorator.option.title
          ),
          outputType = TikzRequirementScoreSpiderDiagram
        )
      )

  val specifications: List[LatexSpecification] =
    scoreSpiderDiagrams ++ securityImpactDiagrams

}
