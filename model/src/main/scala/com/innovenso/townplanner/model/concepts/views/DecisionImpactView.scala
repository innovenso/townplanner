package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{ArchitectureBuildingBlock, BusinessCapability, Decision, Enterprise, HasArchitectureBuildingBlocks, HasBusinessCapabilities, HasDecisions, HasEnterprises, HasItContainers, HasItPlatforms, HasItSystemIntegrations, HasItSystems, HasProjects, HasTechnologies, ItContainer, ItPlatform, ItProject, ItProjectMilestone, ItSystem, ItSystemIntegration, Technology}
import com.innovenso.townplanner.model.concepts.properties.{CanAddProperties, Property}
import com.innovenso.townplanner.model.concepts.relationships.{Association, CanAddRelationships, CanBeImpacted, ChangeImpact, CreateImpact, HasRelationships, KeepImpact, Relationship, RemoveImpact}
import com.innovenso.townplanner.model.language.{CompiledView, HasViews, ModelComponent, TimelessView, View, ViewCompiler}
import com.innovenso.townplanner.model.meta.{ADay, ApplicationLayer, ImplementationLayer, Key, Layer, ModelComponentType, MotivationLayer, SortKey, Today}

case class DecisionImpactView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String = "Decision Impact",
    forDecision: Key,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends TimelessView {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Decision Impact"
  )
  val layer: Layer = MotivationLayer

  def withProperty(property: Property): DecisionImpactView =
    copy(properties = this.properties + (property.key -> property))
}

object DecisionImpactView {
  def apply(
      forDecision: Decision
  ) =
    new DecisionImpactView(
      forDecision = forDecision.key
    )
}

trait HasDecisionImpactViews
    extends HasViews
    with HasBusinessCapabilities
    with HasArchitectureBuildingBlocks
    with HasItSystems
    with HasItPlatforms
    with HasDecisions
    with HasEnterprises
    with HasItSystemIntegrations
    with HasItContainers
    with HasTechnologies {
  def decisionImpactViews: List[CompiledDecisionImpactView] =
    components(
      classOf[DecisionImpactView]
    ).map(view => DecisionImpactViewCompiler(view, this).compile)
  def decisionImpactView(
      key: Key
  ): Option[CompiledDecisionImpactView] =
    component(key, classOf[DecisionImpactView]).map(
      DecisionImpactViewCompiler(_, this).compile
    )

}

trait CanAddDecisionImpactViews
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      decisionImpactView: DecisionImpactView
  ): DecisionImpactView =
    has(decisionImpactView)
}

case class CompiledDecisionImpactView(
    view: DecisionImpactView,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[DecisionImpactView]
    with HasRelationships
    with HasBusinessCapabilities
    with HasArchitectureBuildingBlocks
    with HasItSystems
    with HasDecisions
    with HasEnterprises
    with HasItSystemIntegrations
    with HasItContainers
    with HasTechnologies
    with HasItPlatforms {
  private def impacted[
      ImpactType <: Relationship,
      TargetClassType <: CanBeImpacted
  ](
      impactRelationshipClass: Class[ImpactType],
      targetClass: Class[TargetClassType]
  ): Set[TargetClassType] = relationships
    .filter(impactRelationshipClass.isInstance)
    .flatMap(relationshipParticipants)
    .filter(targetClass.isInstance)
    .map(targetClass.cast)
    .toSet
  def added[TargetClassType <: CanBeImpacted](
      targetClass: Class[TargetClassType]
  ): Set[TargetClassType] = impacted(classOf[CreateImpact], targetClass)
  def removed[TargetClassType <: CanBeImpacted](
      targetClass: Class[TargetClassType]
  ): Set[TargetClassType] = impacted(classOf[RemoveImpact], targetClass)
  def kept[TargetClassType <: CanBeImpacted](
      targetClass: Class[TargetClassType]
  ): Set[TargetClassType] = impacted(classOf[KeepImpact], targetClass)
  def changed[TargetClassType <: CanBeImpacted](
      targetClass: Class[TargetClassType]
  ): Set[TargetClassType] = impacted(classOf[ChangeImpact], targetClass)

  def decision: Decision =
    decisions.headOption.getOrElse(Decision(title = "Unknown Decision"))
}

case class DecisionImpactViewCompiler(
    view: DecisionImpactView,
    source: HasDecisionImpactViews
) extends ViewCompiler[
      DecisionImpactView,
      CompiledDecisionImpactView,
      HasDecisionImpactViews
    ] {
  def compile: CompiledDecisionImpactView =
    CompiledDecisionImpactView(
      view,
      viewTitle,
      groupTitle(view.forDecision),
      viewComponents(
        enterprises ++ decisions ++ capabilities ++ buildingBlocks ++ systems ++ integrations ++ platforms ++ technologies ++ containers ++ relationships
      )
    )

  private def decisions: Set[Decision] = source.decision(view.forDecision).toSet

  private def enterprises: Set[Enterprise] = decisions.flatMap(source.enterprise)

  private def capabilities: Set[BusinessCapability] = impacted(
    classOf[BusinessCapability]
  )

  private def buildingBlocks: Set[ArchitectureBuildingBlock] = impacted(
    classOf[ArchitectureBuildingBlock]
  )

  private def systems: Set[ItSystem] = impacted(classOf[ItSystem])

  private def containers: Set[ItContainer] = impacted(classOf[ItContainer])

  private def integrations: Set[ItSystemIntegration] = impacted(
    classOf[ItSystemIntegration]
  )

  private def platforms: Set[ItPlatform] = impacted(classOf[ItPlatform])

  private def technologies: Set[Technology] = impacted(classOf[Technology])

  private def impacted[TargetClassType <: CanBeImpacted](
      targetClass: Class[TargetClassType]
  ): Set[TargetClassType] = relationships
    .flatMap(source.relationshipParticipants)
    .filter(targetClass.isInstance)
    .map(targetClass.cast)

  private def createImpactRelationships: Set[Relationship] =
    decisions.flatMap(source.relationships(_, classOf[CreateImpact]))

  private def removedImpactRelationships: Set[Relationship] =
    decisions.flatMap(source.relationships(_, classOf[RemoveImpact]))

  private def changeImpactRelationships: Set[Relationship] =
    decisions.flatMap(source.relationships(_, classOf[ChangeImpact]))

  private def keepImpactRelationships: Set[Relationship] =
    decisions.flatMap(source.relationships(_, classOf[KeepImpact]))

  private def relationships: Set[Relationship] =
    createImpactRelationships ++ removedImpactRelationships ++ changeImpactRelationships ++ keepImpactRelationships

}
