package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  BusinessActor,
  BusinessCapability,
  CanBeImplementedByTechnologies,
  Decision,
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
  HasTechnologies,
  ItContainer,
  ItPlatform,
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
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  Accountable,
  CanAddRelationships,
  Composition,
  Consulted,
  HasRelationships,
  Implementation,
  Influence,
  Informed,
  Knowledge,
  RACI,
  Relationship,
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

case class ArchitectureDecisionRecord(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String = "Architecture Decision Record",
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends TimelessView {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Architecture Decision Record"
  )
  val layer: Layer = MotivationLayer

  def withProperty(property: Property): ArchitectureDecisionRecord =
    copy(properties = this.properties + (property.key -> property))

}

trait HasArchitectureDecisionRecords
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
    with HasDecisions
    with HasPrinciples {
  def architectureDecisionRecords: List[CompiledArchitectureDecisionRecord] =
    components(
      classOf[ArchitectureDecisionRecord]
    ).map(view => ArchitectureDecisionRecordCompiler(view, this).compile)
  def architectureDecisionRecord(
      key: Key
  ): Option[CompiledArchitectureDecisionRecord] =
    component(key, classOf[ArchitectureDecisionRecord]).map(
      ArchitectureDecisionRecordCompiler(_, this).compile
    )

}

trait CanAddArchitectureDecisionRecords
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      architectureDecisionRecord: ArchitectureDecisionRecord
  ): ArchitectureDecisionRecord =
    has(architectureDecisionRecord)
}

case class CompiledArchitectureDecisionRecord(
    view: ArchitectureDecisionRecord,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[ArchitectureDecisionRecord]
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
    with HasDecisions
    with HasPrinciples {
  val decoratedDecisions: List[DecisionDecorator] =
    decisions.map(DecisionDecorator(this, _))
}

case class DecisionDecorator(
    view: CompiledArchitectureDecisionRecord,
    decision: Decision
) {
  val hasCurrentConditions: Boolean = decision.currentState.nonEmpty

  val hasDescriptions: Boolean = decision.descriptions.nonEmpty
  val hasGoals: Boolean = decision.goals.nonEmpty
  val hasAssumptions: Boolean =
    decision.assumptions.nonEmpty
  val hasConsequences: Boolean =
    decision.consequences.nonEmpty
  val responsible: List[Person] =
    view.directDependencies(decision, classOf[Responsible], classOf[Person])
  val accountable: List[Person] =
    view.directDependencies(decision, classOf[Accountable], classOf[Person])
  val consulted: List[Person] =
    view.directDependencies(decision, classOf[Consulted], classOf[Person])
  val informed: List[Person] =
    view.directDependencies(decision, classOf[Informed], classOf[Person])
  val stakeholders: List[Person] =
    view.directDependencies(decision, classOf[Stakeholder], classOf[Person])
  val influencers: List[Person] =
    view.directDependencies(decision, classOf[Influence], classOf[Person])
  val influencingPrinciples: List[Principle] =
    view.directDependencies(decision, classOf[Influence], classOf[Principle])

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

  val hasRequirements: Boolean = decision.requirements.nonEmpty
  val hasFunctionalRequirements: Boolean =
    decision.functionalRequirements.nonEmpty
  val hasQualityAttributeRequirements: Boolean =
    decision.qualityAttributeRequirements.nonEmpty
  val hasConstraints: Boolean = decision.constraints.nonEmpty
}

case class ArchitectureDecisionRecordCompiler(
    view: ArchitectureDecisionRecord,
    source: HasArchitectureDecisionRecords
) extends ViewCompiler[
      ArchitectureDecisionRecord,
      CompiledArchitectureDecisionRecord,
      HasArchitectureDecisionRecords
    ] {
  def compile: CompiledArchitectureDecisionRecord =
    CompiledArchitectureDecisionRecord(
      view,
      viewTitle,
      "Architecture Decision Record",
      viewComponents(
        source.modelComponents.values
      )
    )
}
