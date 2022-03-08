package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.{TownPlan, EnterpriseArchitecture}

trait EnterpriseArchitectureContext {
  val ea: EnterpriseArchitecture = EnterpriseArchitecture()

  def exists[ModelComponentType <: ModelComponent](
      modelComponent: ModelComponentType
  ): Boolean = {
    ea.townPlan.has(modelComponent)
  }

  def townPlan: TownPlan = ea.townPlan
}
