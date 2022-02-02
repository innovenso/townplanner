package com.innovenso.townplanner.model.test

import com.innovenso.townplanner.model.TownPlanFactory

trait Factory {
  val factory: TownPlanFactory = new TownPlanFactory
}
