package com.innovenso.townplanner.model

import com.innovenso.townplanner.model.concepts.{HasEnterprises, Relationship}
import com.innovenso.townplanner.model.language.{
  HasModelComponents,
  ModelComponent
}
import com.innovenso.townplanner.model.meta.Key

import java.time.LocalDate

case class TownPlan(
    modelComponents: Map[Key, ModelComponent],
    keyPointsInTime: Map[LocalDate, KeyPointInTime]
) extends HasModelComponents
    with HasKeyPointsInTime
    with HasEnterprises
