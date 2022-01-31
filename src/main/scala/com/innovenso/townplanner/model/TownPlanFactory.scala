package com.innovenso.townplanner.model

import com.innovenso.townplanner.model.factory.{
  CanAddEnterprises,
  CanAddKeyPointsInTime,
  CanManipulateTownPlan
}

class TownPlanFactory
    extends CanManipulateTownPlan
    with CanAddEnterprises
    with CanAddKeyPointsInTime
