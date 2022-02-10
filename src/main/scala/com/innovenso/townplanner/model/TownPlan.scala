package com.innovenso.townplanner.model

import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAddRelationships,
  HasRelationships
}
import com.innovenso.townplanner.model.language.{
  CanAddModelComponents,
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
    with HasRelationships
    with HasTechnologies
    with HasBusinessCapabilities
    with HasBusinessActors
    with HasArchitectureBuildingBlocks
    with HasItPlatforms
    with HasItSystems
    with HasItContainers
    with HasDecisions
    with HasPrinciples

class TownPlanFactory
    extends CanAddModelComponents
    with CanAddKeyPointsInTime
    with CanAddRelationships
    with CanAddEnterprises
    with CanAddTechnologies
    with CanAddBusinessCapabilities
    with CanAddArchitectureBuildingBlocks
    with CanAddBusinessActors
    with CanAddPrinciples
    with CanAddItPlatforms
    with CanAddItSystems
    with CanAddItContainers
    with CanAddDecisions
