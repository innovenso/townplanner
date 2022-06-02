package com.innovenso.townplanner.io.latex.slides

import com.innovenso.townplan.io.context.{Output, OutputContext, Pdf}
import com.innovenso.townplanner.io.latex.model.{
  Book,
  KaoBookLibrary,
  LatexSpecification
}
import com.innovenso.townplanner.io.latex.picture.context.{
  TikzRequirementScoreSpiderDiagram,
  TikzSecurityImpactDiagram
}
import com.innovenso.townplanner.io.latex.slides.context.BeamerThemeConfiguration
import com.innovenso.townplanner.io.latex.slides.model.{
  BeamerDefaultThemeLibrary,
  SlideDeck
}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledArchitectureDecisionRecord,
  CompiledFullTownPlanView,
  CompiledProjectMilestoneOverview,
  CompiledTechnologyRadar
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
          dependencies = dependendencies(adr, outputContext),
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
            dependencies =
              dependencies(projectMilestoneOverview, outputContext),
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

  def dependencies(
      projectMilestoneOverview: CompiledProjectMilestoneOverview,
      outputContext: OutputContext
  ): List[Output] = Nil

  def dependendencies(
      adr: CompiledArchitectureDecisionRecord,
      outputContext: OutputContext
  ): List[Output] = spiderDiagramDependencies(
    adr,
    outputContext
  ) ++ securityImpactDependencies(adr, outputContext)

  def spiderDiagramDependencies(
      adr: CompiledArchitectureDecisionRecord,
      outputContext: OutputContext
  ): List[Output] = adr.decoratedDecisions
    .flatMap(dd => dd.options.map(dop => (dd.decision, dop.option)))
    .flatMap(tup =>
      outputContext.outputs(
        ofFileType = Some(Pdf),
        ofOutputType = Some(TikzRequirementScoreSpiderDiagram),
        forView = Some(adr.view),
        forModelComponents = List(tup._1, tup._2)
      )
    )
    .distinct

  def securityImpactDependencies(
      adr: CompiledArchitectureDecisionRecord,
      outputContext: OutputContext
  ): List[Output] = adr.decoratedDecisions
    .flatMap(decoratedDecision =>
      outputContext.outputs(
        ofFileType = Some(Pdf),
        ofOutputType = Some(TikzSecurityImpactDiagram),
        forView = Some(adr.view),
        forModelComponents = List(decoratedDecision.decision)
      )
    )
    .distinct
}
