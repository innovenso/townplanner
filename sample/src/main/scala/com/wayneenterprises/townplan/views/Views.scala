package com.wayneenterprises.townplan.views

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.properties.{
  Message,
  Request,
  Response
}
import com.innovenso.townplanner.model.concepts.views.{
  ArchitectureBuildingBlockRealizationView,
  BusinessCapabilityMap,
  FlowView,
  FullTownPlanView,
  KnowledgeMatrix,
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

  ea needs FlowView(title = "The Joker Flow") and { it =>
    it has Request(
      "enters Joker search term"
    ) from actors.bruceWayne to systems.bcmsUi
    it has Request(
      "passes on the search request"
    ) from systems.bcmsUi to systems.nemesisMs
    it has Request(
      "looks up information"
    ) from systems.nemesisMs to systems.nemesisDb
    it has Response("Joker profile") from systems.nemesisDb to systems.nemesisMs
    it has Message(
      "requests platform rotation"
    ) from systems.nemesisMs to systems.platformMs
    it has Response("Joker profile") from systems.nemesisMs to systems.bcmsUi
    it has Response("Joker profile") from systems.bcmsUi to actors.bruceWayne
  }

  ea needs TechnologyRadar(title = "Wayne Enterprises Technology Radar")

  ea needs KnowledgeMatrix(forTeam = actors.justiceLeague)
}
