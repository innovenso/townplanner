package com.innovenso.townplanner.model.factory

import com.innovenso.townplanner.model.{KeyPointInTime, TownPlan}
import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.meta.Key

import java.time.LocalDate
import scala.util.{Failure, Success, Try}

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
