package com.wayneenterprises.townplan.views

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.views.{
  ArchitectureBuildingBlockRealizationView,
  BusinessCapabilityMap,
  FullTownPlanView,
  SystemContainerView,
  TechnologyRadar
}
import com.innovenso.townplanner.model.meta.Day
import com.wayneenterprises.townplan.application.{BuildingBlocks, Systems}
import com.wayneenterprises.townplan.business.Actors
import com.wayneenterprises.townplan.strategy.{
  BusinessCapabilities,
  Enterprises
}
import com.wayneenterprises.townplan.technology.Technologies

case class Views()(implicit
    ea: EnterpriseArchitecture,
    capabilities: BusinessCapabilities,
    enterprises: Enterprises,
    actors: Actors,
    buildingBlocks: BuildingBlocks,
    systems: Systems
) {
  ea needs BusinessCapabilityMap(forEnterprise = enterprises.wayneCorp)
  ea needs FullTownPlanView(forEnterprise = enterprises.wayneCorp)

  ea needs ArchitectureBuildingBlockRealizationView(
    forBuildingBlock = buildingBlocks.lairManagement,
    includeContainers = false
  )
  ea needs ArchitectureBuildingBlockRealizationView(
    forBuildingBlock = buildingBlocks.lairManagement,
    includeContainers = true
  )

  ea needs SystemContainerView(forSystem = systems.bcms)
  ea needs SystemContainerView(
    forSystem = systems.bcms,
    pointInTime = Day(2000, 1, 1)
  )
  ea needs SystemContainerView(
    forSystem = systems.bcms,
    pointInTime = Day(2010, 1, 1)
  )
  ea needs SystemContainerView(
    forSystem = systems.bcms,
    pointInTime = Day(2030, 1, 1)
  )

  ea needs TechnologyRadar(title = "Wayne Enterprises")

}
