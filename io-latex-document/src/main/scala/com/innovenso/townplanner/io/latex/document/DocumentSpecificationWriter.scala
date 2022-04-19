package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.{Output, OutputContext, Pdf}
import com.innovenso.townplanner.io.latex.model.{
  Book,
  KaoBookLibrary,
  LatexSpecification
}
import com.innovenso.townplanner.io.latex.picture.context.{
  TikzPicture,
  TikzRequirementScoreSpiderDiagram,
  TikzSecurityImpactDiagram
}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledArchitectureDecisionRecord,
  CompiledFullTownPlanView,
  CompiledTechnologyRadar
}
import com.innovenso.townplanner.model.language.{CompiledView, View}
import document.txt.{
  ArchitectureDecisionRecordDocument,
  FullTownPlan,
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
          latexLibraries = List(KaoBookLibrary),
          outputType = Book
        )
      )
    case technologyRadar: CompiledTechnologyRadar =>
      List(
        LatexSpecification(
          view = technologyRadar,
          latexSourceCode =
            TechnologyRadarDocument(townPlan, technologyRadar).body,
          latexLibraries = List(KaoBookLibrary),
          outputType = Book
        )
      )
    case adr: CompiledArchitectureDecisionRecord =>
      List(
        LatexSpecification(
          view = adr,
          dependencies = dependendencies(adr, outputContext),
          latexSourceCode = ArchitectureDecisionRecordDocument(
            townPlan,
            outputContext,
            adr
          ).body,
          latexLibraries = List(KaoBookLibrary),
          outputType = Book
        )
      )
    case _ => Nil
  }

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
