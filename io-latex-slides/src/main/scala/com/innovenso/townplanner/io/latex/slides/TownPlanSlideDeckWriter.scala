package com.innovenso.townplanner.io.latex.slides

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.language.{CompiledView, View}
import com.innovenso.townplanner.model.meta.Key

case class TownPlanSlideDeckWriter(assetRepository: AssetRepository) {
  def write(townPlan: TownPlan, outputContext: OutputContext): OutputContext =
    outputContext.withOutputs(
      views(townPlan)
        .flatMap(view =>
          SlideDeckSpecificationWriter
            .specifications(townPlan, outputContext, view)
        )
        .flatMap(spec =>
          SlideDeckPdfWriter.slideDecks(spec, assetRepository, outputContext)
        )
    )

  def write(
      townPlan: TownPlan,
      viewKey: String,
      outputContext: OutputContext
  ): OutputContext = outputContext.withOutputs(
    view(townPlan, Key(viewKey)).toList
      .flatMap(modelComponent =>
        SlideDeckSpecificationWriter
          .specifications(townPlan, outputContext, modelComponent)
      )
      .flatMap(spec =>
        SlideDeckPdfWriter.slideDecks(spec, assetRepository, outputContext)
      )
  )

  def view(townPlan: TownPlan, key: Key): List[_ <: CompiledView[_ <: View]] =
    views(townPlan).filter(_.key == key)

  def views(townPlan: TownPlan): List[_ <: CompiledView[_ <: View]] =
    townPlan.fullTownPlanViews ++ townPlan.architectureDecisionRecords

}
