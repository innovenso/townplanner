package com.innovenso.townplanner.model

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddDocumentations,
  CanSetArchitectureVerdict
}
import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.language.{
  HasModelComponents,
  ModelComponent
}
import com.innovenso.townplanner.model.meta.Key

import java.time.LocalDate
import scala.util.{Failure, Success, Try}

case class TownPlan(
    modelComponents: Map[Key, ModelComponent],
    keyPointsInTime: Map[LocalDate, KeyPointInTime]
) extends HasModelComponents
    with HasKeyPointsInTime
    with HasEnterprises
    with HasRelationships
    with HasTechnologies
    with HasBusinessCapabilities
    with HasBusinessActors
    with HasArchitectureBuildingBlocks
    with HasItPlatforms
    with HasItSystems
    with HasItContainers

class TownPlanFactory
    extends CanManipulateTownPlan
    with CanAddEnterprises
    with CanAddKeyPointsInTime
    with CanAddDocumentations
    with CanAddRelationships
    with CanAddTechnologies
    with CanAddBusinessCapabilities
    with CanAddBusinessActors
    with CanAddArchitectureBuildingBlocks
    with CanSetArchitectureVerdict
    with CanAddItPlatforms
    with CanAddItSystems
    with CanAddItContainers

trait CanManipulateTownPlan {
  var townPlan: TownPlan = TownPlan(
    Map.empty[Key, ModelComponent],
    Map.empty[LocalDate, KeyPointInTime]
  )

  protected def withNewModelComponent[ModelComponentType <: ModelComponent](
      modelComponent: ModelComponentType
  ): Try[(TownPlan, ModelComponentType)] = {
    if (townPlan.modelComponents.contains(modelComponent.key))
      Failure(
        new IllegalArgumentException(
          s"the townplan already contains a component with key ${modelComponent.key} of type ${modelComponent.modelComponentType.value}"
        )
      )
    else putModelComponent(modelComponent)
  }

  protected def withUpdatedModelComponent[ModelComponentType <: ModelComponent](
      modelComponent: ModelComponentType
  ): Try[(TownPlan, ModelComponentType)] = {
    if (townPlan.component(modelComponent.key, modelComponent.getClass).isEmpty)
      Failure(
        new IllegalArgumentException(
          s"the town plan does not contain the component to be updated ${modelComponent.key}"
        )
      )
    else putModelComponent(modelComponent)
  }

  private def putModelComponent[ModelComponentType <: ModelComponent](
      modelComponent: ModelComponentType
  ): Try[(TownPlan, ModelComponentType)] = {
    this.townPlan = townPlan.copy(
      townPlan.modelComponents + (modelComponent.key -> modelComponent)
    )
    Success((this.townPlan, modelComponent))
  }
}
