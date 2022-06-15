package com.innovenso.townplanner.io.latex.slides

import com.innovenso.townplan.io.context.{Output, OutputContext}
import com.innovenso.townplanner.io.latex.model.LatexSpecification
import com.innovenso.townplanner.io.latex.slides.context.BeamerThemeConfiguration
import com.innovenso.townplanner.io.latex.slides.model.SlideDeck
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledArchitectureDecisionRecord,
  CompiledFullTownPlanView,
  CompiledProjectMilestoneOverview
}
import com.innovenso.townplanner.model.language.{CompiledView, View}
import latex.slides.txt.{
  DecisionSlideDeck,
  FullTownPlan,
  ProjectMilestoneOverviewSlideDeck
}

object SlideDeckSpecificationWriter {
  def specifications(
      townPlan: TownPlan,
      outputContext: OutputContext,
      view: CompiledView[_ <: View]
  ): List[LatexSpecification] = view match {
    case fullTownPlanView: CompiledFullTownPlanView =>
      List(
        LatexSpecification(
          view = fullTownPlanView,
          dependencies = dependencies(outputContext),
          latexSourceCode = FullTownPlan(fullTownPlanView)(townPlan).body,
          latexLibraries = List(BeamerThemeConfiguration.theme),
          outputType = SlideDeck,
          relatedModelComponents = fullTownPlanView.enterprise.toList
        )
      )
    case adr: CompiledArchitectureDecisionRecord =>
      adr.decoratedDecisions.map(decoratedDecision =>
        LatexSpecification(
          view = adr,
          dependencies = dependencies(outputContext),
          latexSourceCode = DecisionSlideDeck(
            adr,
            decoratedDecision.decision.key,
            outputContext
          )(townPlan).body,
          latexLibraries = List(BeamerThemeConfiguration.theme),
          outputType = SlideDeck,
          relatedModelComponents = List(decoratedDecision.decision),
          filenameAppendix = Some(decoratedDecision.decision.title)
        )
      )
    case projectMilestoneOverview: CompiledProjectMilestoneOverview =>
      projectMilestoneOverview.decoratedProjectMilestone
        .map(decoratedProjectMilestone =>
          LatexSpecification(
            view = projectMilestoneOverview,
            dependencies = dependencies(outputContext),
            latexSourceCode = ProjectMilestoneOverviewSlideDeck(
              projectMilestoneOverview,
              outputContext
            )(townPlan).body,
            latexLibraries = List(BeamerThemeConfiguration.theme),
            outputType = SlideDeck,
            relatedModelComponents = List(
              decoratedProjectMilestone.milestone
            ) ++ decoratedProjectMilestone.project.toList,
            filenameAppendix = Some(decoratedProjectMilestone.milestone.title)
          )
        )
        .toList
    case _ => Nil
  }

  private def dependencies(outputContext: OutputContext): List[Output] =
    outputContext.outputs
}
