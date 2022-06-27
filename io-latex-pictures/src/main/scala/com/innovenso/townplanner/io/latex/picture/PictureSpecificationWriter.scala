package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplanner.io.latex.model.{Book, LatexSpecification}
import com.innovenso.townplanner.io.latex.picture.context.{
  TikzBusinessCapabilityOnePager,
  TikzPicture,
  TikzRequirementScoreSpiderDiagram
}
import com.innovenso.townplanner.io.latex.picture.specifications.{
  ArchitectureDecisionRecordSpecificationFactory,
  ProjectMilestoneOverviewSpecificationFactory
}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledArchitectureDecisionRecord,
  CompiledBusinessCapabilityMap,
  CompiledKnowledgeMatrix,
  CompiledProjectMilestoneOverview
}
import com.innovenso.townplanner.model.language.{CompiledView, View}
import tikz.txt.{
  BusinessCapabilityMapPicture,
  DecisionOptionRequirementScoreSpiderDiagram,
  KnowledgeMatrixDiagram
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
          relatedModelComponents = businessCapabilityMap.enterprise.toList,
          latexSourceCode = BusinessCapabilityMapPicture(
            townPlan,
            businessCapabilityMap,
            BusinessCapabilityLayoutHelper(businessCapabilityMap)
          ).body,
          outputType = TikzBusinessCapabilityOnePager
        )
      )
    case knowledgeMatrix: CompiledKnowledgeMatrix =>
      List(
        LatexSpecification(
          view = knowledgeMatrix,
          relatedModelComponents = knowledgeMatrix.team.toList,
          latexSourceCode = KnowledgeMatrixDiagram(
            knowledgeMatrix,
            townPlan
          ).body,
          outputType = TikzBusinessCapabilityOnePager
        )
      )
    case projectMilestoneOverview: CompiledProjectMilestoneOverview =>
      ProjectMilestoneOverviewSpecificationFactory(
        projectMilestoneOverview,
        townPlan
      ).specifications
    case architectureDecisionRecord: CompiledArchitectureDecisionRecord =>
      ArchitectureDecisionRecordSpecificationFactory(
        architectureDecisionRecord,
        townPlan
      ).specifications
    case _ => Nil
  }
}
