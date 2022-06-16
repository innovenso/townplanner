package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  ArchitectureBuildingBlock,
  BusinessActor,
  BusinessCapability,
  CanBeImplementedByTechnologies,
  Decision,
  DecisionOption,
  Enterprise,
  HasArchitectureBuildingBlocks,
  HasBusinessActors,
  HasBusinessCapabilities,
  HasDecisions,
  HasEnterprises,
  HasItContainers,
  HasItPlatforms,
  HasItSystemIntegrations,
  HasItSystems,
  HasPrinciples,
  HasProjects,
  HasTechnologies,
  ItContainer,
  ItPlatform,
  ItProject,
  ItProjectMilestone,
  ItSystem,
  ItSystemIntegration,
  Language,
  LanguageOrFramework,
  Person,
  Platform,
  Principle,
  Technique,
  Technology,
  Tool
}
import com.innovenso.townplanner.model.concepts.properties.{
  ArchitectureVerdict,
  BeEliminated,
  BeInvestedIn,
  BeMigrated,
  BeTolerated,
  CanAddProperties,
  Property,
  Requirement,
  RequirementScore
}
import com.innovenso.townplanner.model.concepts.relationships.{
  Accountable,
  CanAddRelationships,
  CanBeImpacted,
  ChangeImpact,
  Composition,
  Consulted,
  CreateImpact,
  HasRelationships,
  Impact,
  Implementation,
  Influence,
  Informed,
  KeepImpact,
  Knowledge,
  RACI,
  Relationship,
  RemoveImpact,
  Responsible,
  Serving,
  Stakeholder
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  Concept,
  Element,
  HasViews,
  ModelComponent,
  TimelessView,
  ViewCompiler
}
import com.innovenso.townplanner.model.meta.{
  Amber,
  Green,
  ImplementationLayer,
  Key,
  Layer,
  ModelComponentType,
  MotivationLayer,
  Red,
  Severity,
  SortKey,
  StrategyLayer,
  TechnologyLayer
}

case class ProjectMilestoneOverview(
    key: Key = Key(),
    forProjectMilestone: Key,
    sortKey: SortKey = SortKey.next,
    title: String = "Project Milestone Overview",
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends TimelessView {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Project Milestone Overview"
  )
  val layer: Layer = ImplementationLayer

  def withProperty(property: Property): ProjectMilestoneOverview =
    copy(properties = this.properties + (property.key -> property))

}

object ProjectMilestoneOverview {
  def apply(forProjectMilestone: ItProjectMilestone): ProjectMilestoneOverview =
    new ProjectMilestoneOverview(forProjectMilestone = forProjectMilestone.key)
}

trait HasProjectMilestoneOverviews
    extends HasViews
    with HasBusinessActors
    with HasItPlatforms
    with HasItSystems
    with HasItSystemIntegrations
    with HasItContainers
    with HasTechnologies
    with HasEnterprises
    with HasBusinessCapabilities
    with HasArchitectureBuildingBlocks
    with HasProjects
    with HasPrinciples {
  def projectMilestoneOverviews: List[CompiledProjectMilestoneOverview] =
    components(
      classOf[ProjectMilestoneOverview]
    ).map(view => ProjectMilestoneOverviewCompiler(view, this).compile)
  def projectMilestoneOverview(
      key: Key
  ): Option[CompiledProjectMilestoneOverview] =
    component(key, classOf[ProjectMilestoneOverview]).map(
      ProjectMilestoneOverviewCompiler(_, this).compile
    )

}

trait CanAddProjectMilestoneOverviews
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      projectMilestoneOverview: ProjectMilestoneOverview
  ): ProjectMilestoneOverview =
    has(projectMilestoneOverview)
}

case class CompiledProjectMilestoneOverview(
    view: ProjectMilestoneOverview,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[ProjectMilestoneOverview]
    with HasRelationships
    with HasBusinessActors
    with HasItPlatforms
    with HasItSystems
    with HasItSystemIntegrations
    with HasItContainers
    with HasTechnologies
    with HasEnterprises
    with HasBusinessCapabilities
    with HasArchitectureBuildingBlocks
    with HasProjects
    with HasPrinciples {
  val decoratedProjectMilestone: Option[ProjectMilestoneDecorator] =
    itProjectMilestone(view.forProjectMilestone).map(
      ProjectMilestoneDecorator(this, _)
    )

}

case class ProjectMilestoneDecorator(
    view: CompiledProjectMilestoneOverview,
    milestone: ItProjectMilestone
) {
  def hasCurrentConditions: Boolean = milestone.currentState.nonEmpty

  def hasDescriptions: Boolean = milestone.descriptions.nonEmpty
  def hasLinks: Boolean = milestone.links.nonEmpty
  def hasGoals: Boolean = milestone.goals.nonEmpty
  def hasAssumptions: Boolean =
    milestone.assumptions.nonEmpty
  def hasConsequences: Boolean =
    milestone.consequences.nonEmpty
  def hasSolutions: Boolean = milestone.solutions.nonEmpty
  def hasCounterMeasures: Boolean = milestone.counterMeasures.nonEmpty
  def hasDueDate: Boolean = milestone.dueDate.isDefined
  def hasStartDate: Boolean = milestone.startDate.isDefined
  val responsible: List[Person] =
    view.directDependencies(milestone, classOf[Responsible], classOf[Person])
  val accountable: List[Person] =
    view.directDependencies(milestone, classOf[Accountable], classOf[Person])
  val consulted: List[Person] =
    view.directDependencies(milestone, classOf[Consulted], classOf[Person])
  val informed: List[Person] =
    view.directDependencies(milestone, classOf[Informed], classOf[Person])
  val stakeholders: List[Person] =
    view.directDependencies(milestone, classOf[Stakeholder], classOf[Person])
  val influencers: List[Person] =
    view.directDependencies(milestone, classOf[Influence], classOf[Person])
  val influencingPrinciples: List[Principle] =
    view.directDependencies(milestone, classOf[Influence], classOf[Principle])
  private def impacted[
      ImpactType <: Relationship,
      TargetClassType <: CanBeImpacted
  ](
      impactRelationshipClass: Class[ImpactType],
      targetClass: Class[TargetClassType]
  ): Set[TargetClassType] = view.relationships
    .filter(impactRelationshipClass.isInstance)
    .filter(rel => rel.source.equals(milestone.key))
    .flatMap(view.relationshipParticipants)
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

  val addedIntegrations: Set[ItSystemIntegration] = added(
    classOf[ItSystemIntegration]
  )
  val removedIntegrations: Set[ItSystemIntegration] = removed(
    classOf[ItSystemIntegration]
  )
  val changedIntegrations: Set[ItSystemIntegration] = changed(
    classOf[ItSystemIntegration]
  )

  val hasAddedIntegrations: Boolean = addedIntegrations.nonEmpty
  val hasRemovedIntegrations: Boolean = removedIntegrations.nonEmpty
  val hasChangedIntegrations: Boolean = changedIntegrations.nonEmpty

  val addedSystems: Set[ItSystem] = added(
    classOf[ItSystem]
  )
  val removedSystems: Set[ItSystem] = removed(
    classOf[ItSystem]
  )
  val changedSystems: Set[ItSystem] = changed(
    classOf[ItSystem]
  )

  val hasAddedSystems: Boolean = addedSystems.nonEmpty
  val hasRemovedSystems: Boolean = removedSystems.nonEmpty
  val hasChangedSystems: Boolean = changedSystems.nonEmpty

  val impactedCapabilities: List[BusinessCapability] = view.directDependencies(
    milestone,
    classOf[Impact],
    classOf[BusinessCapability]
  )
  val impactedBuildingBlocks: List[ArchitectureBuildingBlock] =
    view.directDependencies(
      milestone,
      classOf[Impact],
      classOf[ArchitectureBuildingBlock]
    )
  val impactedPlatforms: List[ItPlatform] =
    view.directDependencies(milestone, classOf[Impact], classOf[ItPlatform])
  val impactedSystems: List[ItSystem] =
    view.directDependencies(milestone, classOf[Impact], classOf[ItSystem])
  val impactedContainers: List[ItContainer] =
    view.directDependencies(milestone, classOf[Impact], classOf[ItContainer])
  val impactedIntegrations: List[ItSystemIntegration] = view.directDependencies(
    milestone,
    classOf[Impact],
    classOf[ItSystemIntegration]
  )
  val impactedTechnologies: List[Technology] = view.directDependencies(
    milestone,
    classOf[Impact],
    classOf[Technology]
  )

  val hasResponsible: Boolean = responsible.nonEmpty
  val hasAccountable: Boolean = accountable.nonEmpty
  val hasConsulted: Boolean = consulted.nonEmpty
  val hasInformed: Boolean = informed.nonEmpty
  val hasStakeholders: Boolean = stakeholders.nonEmpty
  val hasInfluencers: Boolean = influencers.nonEmpty
  val hasInfluencingPrinciples: Boolean =
    influencingPrinciples.nonEmpty
  val hasNemawashi: Boolean =
    hasResponsible || hasAccountable || hasConsulted || hasInformed

  val hasContext: Boolean =
    hasNemawashi || hasDescriptions || hasCurrentConditions || hasGoals || hasAssumptions || hasInfluencers || hasInfluencingPrinciples

  val hasRequirements: Boolean = milestone.requirements.nonEmpty
  val hasFunctionalRequirements: Boolean =
    milestone.functionalRequirements.nonEmpty
  val hasQualityAttributeRequirements: Boolean =
    milestone.qualityAttributeRequirements.nonEmpty
  val hasConstraints: Boolean = milestone.constraints.nonEmpty

  val hasSecurityImpact: Boolean = milestone.securityImpacts.nonEmpty
  val hasComplianceImpact: Boolean = milestone.complianceConcerns.nonEmpty
  val project: Option[ItProject] = view.itProject(milestone)
  val hasImpactOnBusinessCapabilities: Boolean = impactedCapabilities.nonEmpty
  val hasImpactOnBuildingBlocks: Boolean = impactedBuildingBlocks.nonEmpty
  val hasImpactOnPlatforms: Boolean = impactedPlatforms.nonEmpty
  val hasImpactOnSystems: Boolean = impactedSystems.nonEmpty
  val hasImpactOnContainers: Boolean = impactedContainers.nonEmpty
  val hasImpactOnIntegrations: Boolean = impactedIntegrations.nonEmpty
  val hasImpactOnTechnologies: Boolean = impactedTechnologies.nonEmpty
  val hasImpact: Boolean =
    hasImpactOnBusinessCapabilities || hasImpactOnBuildingBlocks || hasImpactOnPlatforms || hasImpactOnSystems || hasImpactOnContainers || hasImpactOnIntegrations || hasImpactOnTechnologies
}

case class ProjectMilestoneOverviewCompiler(
    view: ProjectMilestoneOverview,
    source: HasProjectMilestoneOverviews
) extends ViewCompiler[
      ProjectMilestoneOverview,
      CompiledProjectMilestoneOverview,
      HasProjectMilestoneOverviews
    ] {
  def compile: CompiledProjectMilestoneOverview =
    CompiledProjectMilestoneOverview(
      view,
      viewTitle,
      "Project Milestone Overview",
      viewComponents(
        source.modelComponents.values
      )
    )
}
