package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{ArchitectureBuildingBlock, BusinessCapability, Enterprise, HasArchitectureBuildingBlocks, HasBusinessCapabilities, HasEnterprises, HasItContainers, HasItPlatforms, HasItSystemIntegrations, HasItSystems, HasProjects, HasTechnologies, ItContainer, ItPlatform, ItProject, ItProjectMilestone, ItSystem, ItSystemIntegration, Technology}
import com.innovenso.townplanner.model.concepts.properties.{CanAddProperties, Property}
import com.innovenso.townplanner.model.concepts.relationships.{Association, CanAddRelationships, CanBeImpacted, ChangeImpact, CreateImpact, HasRelationships, KeepImpact, Relationship, RemoveImpact}
import com.innovenso.townplanner.model.language.{CompiledView, HasViews, ModelComponent, View, ViewCompiler}
import com.innovenso.townplanner.model.meta.{ADay, ApplicationLayer, ImplementationLayer, Key, Layer, ModelComponentType, SortKey, Today}

case class ProjectMilestoneImpactView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String = "Project Milestone Impact",
    forProjectMilestone: Key,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends View {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Project Milestone Impact"
  )
  val layer: Layer = ImplementationLayer
  val pointInTime: ADay = Today

  def withProperty(property: Property): ProjectMilestoneImpactView =
    copy(properties = this.properties + (property.key -> property))
}

trait HasProjectMilestoneImpactViews
    extends HasViews
    with HasBusinessCapabilities
    with HasArchitectureBuildingBlocks
    with HasItSystems
    with HasProjects
    with HasEnterprises
    with HasItSystemIntegrations {
  def projectMilestoneImpactViews: List[CompiledProjectMilestoneImpactView] =
    components(
      classOf[ProjectMilestoneImpactView]
    ).map(view => ProjectMilestoneImpactViewCompiler(view, this).compile)
  def projectMilestoneImpactView(
      key: Key
  ): Option[CompiledProjectMilestoneImpactView] =
    component(key, classOf[ProjectMilestoneImpactView]).map(
      ProjectMilestoneImpactViewCompiler(_, this).compile
    )

}

trait CanAddProjectMilestoneImpactViews
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      projectMilestoneImpactView: ProjectMilestoneImpactView
  ): ProjectMilestoneImpactView =
    has(projectMilestoneImpactView)
}

case class CompiledProjectMilestoneImpactView(
    view: ProjectMilestoneImpactView,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[ProjectMilestoneImpactView]
    with HasRelationships
    with HasBusinessCapabilities
    with HasArchitectureBuildingBlocks
    with HasItSystems
    with HasProjects
    with HasEnterprises
    with HasItSystemIntegrations
    with HasItContainers
    with HasTechnologies
    with HasItPlatforms {
  private def impacted[ImpactType <: Relationship, TargetClassType <: CanBeImpacted](impactRelationshipClass: Class[ImpactType], targetClass: Class[TargetClassType]): Set[TargetClassType] = relationships.filter(impactRelationshipClass.isInstance).flatMap(relationshipParticipants).filter(targetClass.isInstance).map(targetClass.cast).toSet
  private def added[TargetClassType <: CanBeImpacted](targetClass: Class[TargetClassType]): Set[TargetClassType] = impacted(classOf[CreateImpact], targetClass)
  private def removed[TargetClassType <: CanBeImpacted](targetClass: Class[TargetClassType]): Set[TargetClassType] = impacted(classOf[CreateImpact], targetClass)
  private def kept[TargetClassType <: CanBeImpacted](targetClass: Class[TargetClassType]): Set[TargetClassType] = impacted(classOf[CreateImpact], targetClass)
  private def changed[TargetClassType <: CanBeImpacted](targetClass: Class[TargetClassType]): Set[TargetClassType] = impacted(classOf[CreateImpact], targetClass)

  def addedSystems: Set[ItSystem] = added(classOf[ItSystem])
  def removedSystems: Set[ItSystem] = removed(classOf[ItSystem])
  def keptSystems: Set[ItSystem] = kept(classOf[ItSystem])
  def changedSystems: Set[ItSystem] = changed(classOf[ItSystem])
  def addedSystemIntegrations: Set[ItSystemIntegration] = added(classOf[ItSystemIntegration])
  def removedSystemIntegrations: Set[ItSystemIntegration] = removed(classOf[ItSystemIntegration])
  def keptSystemIntegrations: Set[ItSystemIntegration] = kept(classOf[ItSystemIntegration])
  def changedSystemIntegrations: Set[ItSystemIntegration] = changed(classOf[ItSystemIntegration])
  def addedBusinessCapabilities: Set[BusinessCapability] = added(classOf[BusinessCapability])
  def removedBusinessCapabilities: Set[BusinessCapability] = removed(classOf[BusinessCapability])
  def keptBusinessCapabilities: Set[BusinessCapability] = kept(classOf[BusinessCapability])
  def changedBusinessCapabilities: Set[BusinessCapability] = changed(classOf[BusinessCapability])
  def addedArchitectureBuildingBlocks: Set[ArchitectureBuildingBlock] = added(classOf[ArchitectureBuildingBlock])
  def removedArchitectureBuildingBlocks: Set[ArchitectureBuildingBlock] = removed(classOf[ArchitectureBuildingBlock])
  def keptArchitectureBuildingBlocks: Set[ArchitectureBuildingBlock] = kept(classOf[ArchitectureBuildingBlock])
  def changedArchitectureBuildingBlocks: Set[ArchitectureBuildingBlock] = changed(classOf[ArchitectureBuildingBlock])
  def addedPlatforms: Set[ItPlatform] = added(classOf[ItPlatform])
  def removedPlatforms: Set[ItPlatform] = removed(classOf[ItPlatform])
  def keptPlatforms: Set[ItPlatform] = kept(classOf[ItPlatform])
  def changedPlatforms: Set[ItPlatform] = changed(classOf[ItPlatform])
  def addedContainers: Set[ItContainer] = added(classOf[ItContainer])
  def removedContainers: Set[ItContainer] = removed(classOf[ItContainer])
  def keptContainers: Set[ItContainer] = kept(classOf[ItContainer])
  def changedContainers: Set[ItContainer] = changed(classOf[ItContainer])
  def addedTechnologies: Set[Technology] = added(classOf[Technology])
  def removedTechnologies: Set[Technology] = removed(classOf[Technology])
  def keptTechnologies: Set[Technology] = kept(classOf[Technology])
  def changedTechnologies: Set[Technology] = changed(classOf[Technology])
}

case class ProjectMilestoneImpactViewCompiler(
    view: ProjectMilestoneImpactView,
    source: HasProjectMilestoneImpactViews
) extends ViewCompiler[
      ProjectMilestoneImpactView,
      CompiledProjectMilestoneImpactView,
      HasProjectMilestoneImpactViews
    ] {
  def compile: CompiledProjectMilestoneImpactView =
    CompiledProjectMilestoneImpactView(
      view,
      viewComponents(
        enterprises ++ projects ++ milestones ++ capabilities ++ buildingBlocks ++ systems ++ integrations ++ platforms ++ technologies ++ containers ++ relationships
      )
    )

  private def milestones: Set[ItProjectMilestone] =
    source.itProjectMilestone(view.forProjectMilestone).toSet

  private def projects: Set[ItProject] = milestones.flatMap(source.itProject)

  private def enterprises: Set[Enterprise] = projects.flatMap(source.enterprise)

  private def capabilities: Set[BusinessCapability] = impacted(classOf[BusinessCapability])

  private def buildingBlocks: Set[ArchitectureBuildingBlock] = impacted(classOf[ArchitectureBuildingBlock])

  private def systems: Set[ItSystem] = impacted(classOf[ItSystem])

  private def containers: Set[ItContainer] = impacted(classOf[ItContainer])

  private def integrations: Set[ItSystemIntegration] = impacted(classOf[ItSystemIntegration])

  private def platforms: Set[ItPlatform] = impacted(classOf[ItPlatform])

  private def technologies: Set[Technology] = impacted(classOf[Technology])

  private def impacted[TargetClassType <: CanBeImpacted](targetClass: Class[TargetClassType]): Set[TargetClassType] = relationships.flatMap(source.relationshipParticipants).filter(targetClass.isInstance).map(targetClass.cast)

  private def createImpactRelationships: Set[Relationship] = milestones.flatMap(source.relationships(_, classOf[CreateImpact]))

  private def removedImpactRelationships: Set[Relationship] = milestones.flatMap(source.relationships(_, classOf[RemoveImpact]))

  private def changeImpactRelationships: Set[Relationship] = milestones.flatMap(source.relationships(_, classOf[ChangeImpact]))

  private def keepImpactRelationships: Set[Relationship] = milestones.flatMap(source.relationships(_, classOf[KeepImpact]))

  private def relationships: Set[Relationship] = createImpactRelationships ++ removedImpactRelationships ++ changeImpactRelationships ++ keepImpactRelationships

}
