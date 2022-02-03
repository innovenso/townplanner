package com.innovenso.townplanner.model.test

import com.innovenso.townplanner.model.{TownPlan, TownPlanFactory}
import com.innovenso.townplanner.model.language.ModelComponent

import scala.util.{Failure, Success, Try}

trait Factory {
  val factory: TownPlanFactory = new TownPlanFactory
}
