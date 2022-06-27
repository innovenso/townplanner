package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.{Output, OutputContext, Pdf}
import com.innovenso.townplanner.io.latex.document.context.BookThemeConfiguration
import com.innovenso.townplanner.io.latex.document.model.LegrangeBookLibrary
import com.innovenso.townplanner.io.latex.model.{Book, LatexSpecification}
import com.innovenso.townplanner.io.latex.picture.context.{
  TikzPicture,
  TikzRequirementScoreSpiderDiagram,
  TikzSecurityImpactDiagram
}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledArchitectureDecisionRecord,
  CompiledFullTownPlanView,
  CompiledProjectMilestoneOverview,
  CompiledTechnologyRadar
}
import com.innovenso.townplanner.model.language.{CompiledView, View}
import document.txt.{
  ArchitectureDecisionRecordDocument,
  FullTownPlan,
  ProjectMilestoneOverviewDocument,
  TechnologyRadarDocument
}

object DocumentSpecificationWriter {
  def specifications(
      townPlan: TownPlan,
      outputContext: OutputContext,
      view: CompiledView[_ <: View]
  ): List[LatexSpecification] = view match {
    case fullTownPlanView: CompiledFullTownPlanView =>
      List(
        LatexSpecification(
          view = fullTownPlanView,
          latexSourceCode = FullTownPlan(townPlan, fullTownPlanView).body,
          latexLibraries =
            List(LegrangeBookLibrary, BookThemeConfiguration.theme),
          outputType = Book,
          relatedModelComponents = fullTownPlanView.enterprise.toList
        )
      )
    case technologyRadar: CompiledTechnologyRadar =>
      List(
        LatexSpecification(
          view = technologyRadar,
          latexSourceCode =
            TechnologyRadarDocument(townPlan, technologyRadar).body,
          latexLibraries =
            List(LegrangeBookLibrary, BookThemeConfiguration.theme),
          outputType = Book,
          relatedModelComponents = technologyRadar.enterprises
        )
      )
    case adr: CompiledArchitectureDecisionRecord =>
      List(
        LatexSpecification(
          view = adr,
          dependencies = dependencies(outputContext),
          latexSourceCode = ArchitectureDecisionRecordDocument(
            townPlan,
            outputContext,
            adr
          ).body,
          latexLibraries =
            List(LegrangeBookLibrary, BookThemeConfiguration.theme),
          outputType = Book,
          relatedModelComponents = adr.enterprises
        )
      )
    case projectMilestoneOverview: CompiledProjectMilestoneOverview =>
      projectMilestoneOverview.decoratedProjectMilestone
        .map(decoratedProjectMilestone =>
          LatexSpecification(
            view = projectMilestoneOverview,
            dependencies = dependencies(outputContext),
            latexSourceCode = ProjectMilestoneOverviewDocument(
              townPlan,
              outputContext,
              projectMilestoneOverview
            ).body,
            latexLibraries =
              List(LegrangeBookLibrary, BookThemeConfiguration.theme),
            outputType = Book,
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
