package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.language.{
  CompiledView,
  ModelComponent,
  View
}
import com.innovenso.townplanner.model.meta.Key
import com.typesafe.scalalogging.LazyLogging

import java.nio.file.Path

case class TownPlanDiagramWriter(
    targetBasePath: Path,
    assetRepository: AssetRepository
) extends LazyLogging {

  def views(townPlan: TownPlan): List[_ <: CompiledView[_ <: View]] =
    townPlan.systemContainerViews ++ townPlan.systemIntegrationViews ++ townPlan.flowViews

  def view(townPlan: TownPlan, key: Key): Option[_ <: CompiledView[_ <: View]] =
    views(townPlan).find(_.key == key)

  def write(townPlan: TownPlan, outputContext: OutputContext): OutputContext =
    outputContext.withOutputs(
      views(townPlan)
        .flatMap(view =>
          DiagramSpecificationWriter.specifications(townPlan, view)
        )
        .flatMap(spec => DiagramImageWriter.diagrams(spec, assetRepository))
    )

  def write(
      townPlan: TownPlan,
      viewKey: String,
      outputContext: OutputContext
  ): OutputContext = outputContext.withOutputs(
    view(townPlan, Key(viewKey)).toList
      .flatMap(modelComponent =>
        DiagramSpecificationWriter.specifications(townPlan, modelComponent)
      )
      .flatMap(spec => DiagramImageWriter.diagrams(spec, assetRepository))
  )

}