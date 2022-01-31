package com.innovenso.townplanner.model.traits

import com.innovenso.townplanner.model.{KeyPointInTime, TownPlan}
import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.meta.Key

import java.time.LocalDate

trait HasTownPlan {
  var townPlan: TownPlan = TownPlan(Map.empty[Key,ModelComponent], Map.empty[LocalDate,KeyPointInTime])

  protected def withModelComponent(modelComponent: ModelComponent): TownPlan = {
    this.townPlan = townPlan.copy(townPlan.modelComponents + (modelComponent.key -> modelComponent))
    this.townPlan
  }

}
