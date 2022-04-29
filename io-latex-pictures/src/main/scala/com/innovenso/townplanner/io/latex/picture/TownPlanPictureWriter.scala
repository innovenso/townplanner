package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.language.{CompiledView, View}
import com.innovenso.townplanner.model.meta.Key

case class TownPlanPictureWriter(assetRepository: AssetRepository) {
  def write(townPlan: TownPlan, outputContext: OutputContext): OutputContext =
    outputContext.withOutputs(
      views(townPlan)
        .flatMap(view =>
          PictureSpecificationWriter
            .specifications(townPlan, view)
        )
        .flatMap(spec =>
          PicturePdfWriter.pictures(spec, assetRepository, outputContext)
        )
    )

  def write(
      townPlan: TownPlan,
      viewKey: String,
      outputContext: OutputContext
  ): OutputContext = outputContext.withOutputs(
    view(townPlan, Key(viewKey)).toList
      .flatMap(modelComponent =>
        PictureSpecificationWriter.specifications(townPlan, modelComponent)
      )
      .flatMap(spec =>
        PicturePdfWriter.pictures(spec, assetRepository, outputContext)
      )
  )

  def view(townPlan: TownPlan, key: Key): Option[_ <: CompiledView[_ <: View]] =
    views(townPlan).find(_.key == key)

  def views(townPlan: TownPlan): List[_ <: CompiledView[_ <: View]] =
    townPlan.businessCapabilityMaps ++ townPlan.fullTownPlanViews ++ townPlan.projectMilestoneImpactViews ++ townPlan.technologyRadars ++ townPlan.architectureDecisionRecords ++ townPlan.knowledgeMatrices
}
