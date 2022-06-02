package com.innovenso.townplanner.io.latex.picture.specifications

import com.innovenso.townplanner.io.latex.model.LatexSpecification
import com.innovenso.townplanner.io.latex.picture.context.TikzSecurityImpactDiagram
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.CompiledProjectMilestoneOverview
import tikz.txt.SecurityImpactDiagram

case class ProjectMilestoneOverviewSpecificationFactory(
    projectMilestoneOverview: CompiledProjectMilestoneOverview,
    townPlan: TownPlan
) {

  val securityImpactDiagrams: List[LatexSpecification] =
    projectMilestoneOverview.decoratedProjectMilestone
      .map(projectMilestoneDecorator =>
        LatexSpecification(
          view = projectMilestoneOverview,
          relatedModelComponents = List(projectMilestoneDecorator.milestone),
          latexSourceCode = SecurityImpactDiagram(
            townPlan,
            projectMilestoneDecorator.milestone,
            projectMilestoneDecorator.milestone.title
          ).body,
          filenameAppendix = Some(projectMilestoneDecorator.milestone.title),
          outputType = TikzSecurityImpactDiagram
        )
      )
      .toList

  val specifications: List[LatexSpecification] = securityImpactDiagrams

}
