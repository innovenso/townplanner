package com.wayneenterprises.townplan

import com.innovenso.townplan.application.EnterpriseArchitectureAsCode
import com.wayneenterprises.townplan.application.{BuildingBlocks, Systems}
import com.wayneenterprises.townplan.business.Actors
import com.wayneenterprises.townplan.strategy.{
  BusinessCapabilities,
  Enterprises
}
import com.wayneenterprises.townplan.technology.TechnologyRadar
import com.wayneenterprises.townplan.views.Views

object WayneEnterprisesTownPlan extends EnterpriseArchitectureAsCode {
  implicit val enterprises: Enterprises = Enterprises()
  implicit val capabilities: BusinessCapabilities = BusinessCapabilities()
  implicit val technologyRadar: TechnologyRadar = TechnologyRadar()
  implicit val actors: Actors = Actors()
  implicit val buildingBlocks: BuildingBlocks = BuildingBlocks()
  implicit val systems: Systems = Systems()

  val views: Views = Views()

  diagrams
}
