package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  ArchitectureBuildingBlock,
  BusinessCapability,
  Enterprise,
  HasArchitectureBuildingBlocks,
  HasBusinessCapabilities,
  HasEnterprises,
  HasItSystemIntegrations,
  HasItSystems,
  HasProjects,
  ItProject,
  ItProjectMilestone,
  ItSystem,
  ItSystemIntegration
}
import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  Association,
  CanAddRelationships,
  HasRelationships,
  Relationship
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  HasViews,
  ModelComponent,
  View,
  ViewCompiler
}
import com.innovenso.townplanner.model.meta.{
  ADay,
  ApplicationLayer,
  ImplementationLayer,
  Key,
  Layer,
  ModelComponentType,
  SortKey,
  Today
}

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
    with HasItSystemIntegrations {}

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
        enterprise ++ projects ++ milestones ++ capabilities ++ buildingBlocks ++ systems ++ integrations ++ relationships
      )
    )

  private def milestones: Set[ItProjectMilestone] =
    source.itProjectMilestone(view.forProjectMilestone).toSet

  private def projects: Set[ItProject] = milestones.map(source.itProject(_))

  private def enterprise: Set[Enterprise] = projects.map(source.enterprise(_))

  private def capabilities: Set[BusinessCapability] = ???

  private def buildingBlocks: Set[ArchitectureBuildingBlock] = ???

  private def systems: Set[ItSystem] = ???

  private def integrations: Set[ItSystemIntegration] = ???

  private def relationships: Set[Relationship] = ???

}
