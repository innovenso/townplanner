package com.innovenso.townplanner.model

import com.innovenso.townplanner.model.concepts.Relationship
import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.meta.Key

import java.time.LocalDate

case class TownPlan(modelComponents: Map[Key, ModelComponent], keyPointsInTime: Map[LocalDate, KeyPointInTime]) {

}
