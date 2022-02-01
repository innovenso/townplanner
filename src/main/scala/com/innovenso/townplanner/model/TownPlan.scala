package com.innovenso.townplanner.model

import com.innovenso.townplanner.model.concepts.properties.CanAddDocumentations
import com.innovenso.townplanner.model.concepts.{
  CanAddEnterprises,
  CanAddKeyPointsInTime,
  HasEnterprises,
  HasKeyPointsInTime,
  KeyPointInTime
}
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

trait CanManipulateTownPlan {
  var townPlan: TownPlan = TownPlan(
    Map.empty[Key, ModelComponent],
    Map.empty[LocalDate, KeyPointInTime]
  )

  protected def withModelComponent(
      modelComponent: ModelComponent
  ): Try[TownPlan] = {
    if (townPlan.modelComponents.contains(modelComponent.key))
      Failure(
        new IllegalArgumentException(
          s"the townplan already contains an element with key ${modelComponent.key} of type ${modelComponent.modelComponentType.value}"
        )
      )
    else {
      this.townPlan = townPlan.copy(
        townPlan.modelComponents + (modelComponent.key -> modelComponent)
      )
      Success(this.townPlan)
    }
  }
}

class TownPlanFactory
    extends CanManipulateTownPlan
    with CanAddEnterprises
    with CanAddKeyPointsInTime
    with CanAddDocumentations
