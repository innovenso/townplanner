package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  BusinessActor,
  HasBusinessActors,
  HasItContainers,
  HasItSystems,
  HasProjects,
  HasTechnologies,
  ItContainer,
  ItProject,
  ItProjectMilestone,
  ItSystem,
  Technology
}
import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAddRelationships,
  CanBeImpacted,
  ChangeImpact,
  Composition,
  CreateImpact,
  Flow,
  HasRelationships,
  Implementation,
  KeepImpact,
  Relationship,
  RemoveImpact
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  Element,
  HasViews,
  ModelComponent,
  TimelessView,
  ViewCompiler
}
import com.innovenso.townplanner.model.meta.{
  ApplicationLayer,
  ImplementationLayer,
  Key,
  Layer,
  ModelComponentType,
  SortKey
}

case class ProjectMilestoneTransitionSystemContainerView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String = "Project Milestone System Container View",
    forProjectMilestone: Key,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends TimelessView {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Project Milestone System Container View"
  )
  val layer: Layer = ApplicationLayer

  def withProperty(
      property: Property
  ): ProjectMilestoneTransitionSystemContainerView =
    copy(properties = this.properties + (property.key -> property))
}

object ProjectMilestoneTransitionSystemContainerView {
  def apply(
      forProjectMilestone: ItProjectMilestone
  ) =
    new ProjectMilestoneTransitionSystemContainerView(
      forProjectMilestone = forProjectMilestone.key
    )
}

trait CanAddProjectMilestoneTransitionSystemContainerViews
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      projectMilestoneTransitionSystemContainerView: ProjectMilestoneTransitionSystemContainerView
  ): ProjectMilestoneTransitionSystemContainerView =
    has(projectMilestoneTransitionSystemContainerView)
}

trait HasProjectMilestoneTransitionSystemContainerViews
    extends HasViews
    with HasItSystems
    with HasItContainers
    with HasRelationships
    with HasBusinessActors
    with HasTechnologies
    with HasProjects {
  def beforeProjectMilestoneSystemContainerViews
      : List[CompiledProjectMilestoneTransitionSystemContainerView] =
    components(
      classOf[ProjectMilestoneTransitionSystemContainerView]
    ).map(view =>
      ProjectMilestoneTransitionSystemContainerViewCompiler(
        view,
        this,
        isBefore = true
      ).compile
    )
  def beforeProjectMilestoneSystemContainerView(
      key: Key
  ): Option[CompiledProjectMilestoneTransitionSystemContainerView] =
    component(key, classOf[ProjectMilestoneTransitionSystemContainerView]).map(
      ProjectMilestoneTransitionSystemContainerViewCompiler(
        _,
        this,
        isBefore = true
      ).compile
    )
  def afterProjectMilestoneSystemContainerViews
      : List[CompiledProjectMilestoneTransitionSystemContainerView] =
    components(
      classOf[ProjectMilestoneTransitionSystemContainerView]
    ).map(view =>
      ProjectMilestoneTransitionSystemContainerViewCompiler(
        view,
        this,
        isBefore = false
      ).compile
    )
  def afterProjectMilestoneSystemContainerView(
      key: Key
  ): Option[CompiledProjectMilestoneTransitionSystemContainerView] =
    component(key, classOf[ProjectMilestoneTransitionSystemContainerView]).map(
      ProjectMilestoneTransitionSystemContainerViewCompiler(
        _,
        this,
        isBefore = false
      ).compile
    )
}

case class CompiledProjectMilestoneTransitionSystemContainerView(
    view: ProjectMilestoneTransitionSystemContainerView,
    title: String,
    groupTitle: String,
    isBefore: Boolean,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[ProjectMilestoneTransitionSystemContainerView]
    with HasItSystems
    with HasItContainers
    with HasRelationships
    with HasBusinessActors
    with HasTechnologies
    with HasProjects {
  val systemContexts: List[ItSystem] =
    containers.map(system).filter(_.nonEmpty).map(_.get)
  val otherSystems: List[ItSystem] = systems.filterNot(systemContexts.toSet)

  val flows: List[Flow] = relationshipsWithType(classOf[Flow])
  val milestone: ItProjectMilestone = itProjectMilestone(
    view.forProjectMilestone
  ).getOrElse(ItProjectMilestone(title = "Unknown Milestone"))

}

case class ProjectMilestoneTransitionSystemContainerViewCompiler(
    view: ProjectMilestoneTransitionSystemContainerView,
    source: HasProjectMilestoneTransitionSystemContainerViews,
    isBefore: Boolean
) extends ViewCompiler[
      ProjectMilestoneTransitionSystemContainerView,
      CompiledProjectMilestoneTransitionSystemContainerView,
      HasProjectMilestoneTransitionSystemContainerViews
    ] {
  override def compile: CompiledProjectMilestoneTransitionSystemContainerView =
    CompiledProjectMilestoneTransitionSystemContainerView(
      view,
      viewTitle,
      groupTitle(view.forProjectMilestone),
      isBefore,
      viewComponents(
        milestones ++ projects ++ systemContexts ++ simpleSystems ++ containers ++ actors ++ flows ++ technologies ++ implementingTechnologies ++ compositions
      )
    )

  private val milestones: Set[ItProjectMilestone] =
    source.itProjectMilestone(view.forProjectMilestone).toSet

  private val projects: Set[ItProject] = milestones.flatMap(source.itProject)
  private val containers: Set[ItContainer] = impacted(classOf[ItContainer])
  private val systemContexts: Set[ItSystem] = containers.flatMap(source.system)
  private val simpleSystems: Set[ItSystem] =
    impacted(classOf[ItSystem]).filterNot(systemContexts)
  private val actors: Set[BusinessActor] =
    (containers ++ simpleSystems).flatMap(element =>
      source.directDependencies(element, classOf[Flow], classOf[BusinessActor])
    )
  private val technologies: Set[Technology] =
    containers.flatMap(source.technologies)

  private val compositions: Set[Relationship] = containers.flatMap(container =>
    source.relationships(container, classOf[Composition], classOf[ItSystem])
  )

  private val flowParticipants: Set[Element] =
    (actors ++ containers ++ simpleSystems).map(_.asInstanceOf[Element])
  private val flows: Set[Relationship] = flowParticipants
    .flatMap(participant => source.relationships(participant, classOf[Flow]))
    .filter(relationship =>
      source
        .relationshipParticipants(relationship)
        .forall(flowParticipants.contains)
    )
  private val implementingTechnologies: Set[Relationship] =
    containers.flatMap(container =>
      source.relationships(
        container,
        classOf[Implementation],
        classOf[Technology]
      )
    )

  private def impacted[TargetClassType <: CanBeImpacted](
      targetClass: Class[TargetClassType]
  ): Set[TargetClassType] = impactRelationships
    .flatMap(source.relationshipParticipants)
    .filter(targetClass.isInstance)
    .map(targetClass.cast)

  private def createImpactRelationships: Set[Relationship] = if (isBefore) Set()
  else
    milestones.flatMap(source.relationships(_, classOf[CreateImpact]))

  private def removedImpactRelationships: Set[Relationship] = if (isBefore)
    milestones.flatMap(source.relationships(_, classOf[RemoveImpact]))
  else Set()

  private def changeImpactRelationships: Set[Relationship] =
    milestones.flatMap(source.relationships(_, classOf[ChangeImpact]))

  private def keepImpactRelationships: Set[Relationship] =
    milestones.flatMap(source.relationships(_, classOf[KeepImpact]))

  private def impactRelationships: Set[Relationship] =
    createImpactRelationships ++ removedImpactRelationships ++ changeImpactRelationships ++ keepImpactRelationships

}
