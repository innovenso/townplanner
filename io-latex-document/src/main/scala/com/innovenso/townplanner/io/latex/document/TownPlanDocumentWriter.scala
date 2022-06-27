package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.io.state.{NoStateRepository, StateRepository}
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.{
  DiagramImageWriter,
  DiagramSpecificationWriter
}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.language.{CompiledView, View}
import com.innovenso.townplanner.model.meta.Key

case class TownPlanDocumentWriter(
    assetRepository: AssetRepository,
    stateRepository: StateRepository
) {
  def write(townPlan: TownPlan, outputContext: OutputContext): OutputContext =
    outputContext.withOutputs(
      views(townPlan)
        .flatMap(view =>
          DocumentSpecificationWriter
            .specifications(townPlan, outputContext, view)
        )
        .flatMap(spec =>
          DocumentPdfWriter
            .documents(spec, assetRepository, outputContext, stateRepository)
        )
    )

  def write(
      townPlan: TownPlan,
      viewKey: String,
      outputContext: OutputContext
  ): OutputContext = outputContext.withOutputs(
    view(townPlan, Key(viewKey)).toList
      .flatMap(modelComponent =>
        DocumentSpecificationWriter
          .specifications(townPlan, outputContext, modelComponent)
      )
      .flatMap(spec =>
        DocumentPdfWriter
          .documents(spec, assetRepository, outputContext, NoStateRepository())
      )
  )

  def view(townPlan: TownPlan, key: Key): Option[_ <: CompiledView[_ <: View]] =
    views(townPlan).find(_.key == key)

  def views(townPlan: TownPlan): List[_ <: CompiledView[_ <: View]] =
    townPlan.businessCapabilityMaps ++ townPlan.fullTownPlanViews ++ townPlan.projectMilestoneImpactViews ++ townPlan.technologyRadars ++ townPlan.architectureDecisionRecords ++ townPlan.projectMilestoneOverviews

}
