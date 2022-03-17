package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  BusinessActor,
  BusinessCapability,
  CanBeImplementedByTechnologies,
  Enterprise,
  HasBusinessActors,
  HasBusinessCapabilities,
  HasEnterprises,
  HasItContainers,
  HasItPlatforms,
  HasItSystems,
  HasTechnologies,
  ItContainer,
  ItPlatform,
  ItSystem,
  Technology
}
import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAddRelationships,
  Composition,
  HasRelationships,
  Implementation,
  Knowledge,
  Serving
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
  Amber,
  Green,
  Key,
  Layer,
  ModelComponentType,
  Red,
  Severity,
  SortKey,
  StrategyLayer,
  TechnologyLayer
}

case class TechnologyRadar(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String = "Technology Radar",
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends TimelessView {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Technology Radar"
  )
  val layer: Layer = TechnologyLayer

  def withProperty(property: Property): TechnologyRadar =
    copy(properties = this.properties + (property.key -> property))

}

trait HasTechnologyRadars
    extends HasViews
    with HasBusinessActors
    with HasItPlatforms
    with HasItSystems
    with HasItContainers
    with HasTechnologies {
  def technologyRadars: List[CompiledTechnologyRadar] = components(
    classOf[TechnologyRadar]
  ).map(view => TechnologyRadarCompiler(view, this).compile)
  def technologyRadar(key: Key): Option[CompiledTechnologyRadar] =
    component(key, classOf[TechnologyRadar]).map(
      TechnologyRadarCompiler(_, this).compile
    )

}

trait CanAddTechnologyRadars extends CanAddProperties with CanAddRelationships {
  def needs(
      technologyRadar: TechnologyRadar
  ): TechnologyRadar =
    has(technologyRadar)
}

case class CompiledTechnologyRadar(
    view: TechnologyRadar,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[TechnologyRadar]
    with HasRelationships
    with HasBusinessActors
    with HasItPlatforms
    with HasItSystems
    with HasItContainers
    with HasTechnologies {
  def containersImplementedWith(technology: Technology): Set[ItContainer] = {
    relationships(technology, classOf[Implementation], classOf[ItContainer])
      .flatMap(relationshipParticipantsOfType(_, classOf[ItContainer]))
      .toSet
  }
  def systemsImplementedWith(technology: Technology): Set[ItSystem] =
    containersImplementedWith(technology)
      .flatMap(relationships(_, classOf[Composition], classOf[ItSystem]))
      .flatMap(relationshipParticipantsOfType(_, classOf[ItSystem]))
  def platformsImplementedWith(technology: Technology): Set[ItPlatform] =
    systemsImplementedWith(technology)
      .flatMap(relationships(_, classOf[Composition], classOf[ItPlatform]))
      .flatMap(relationshipParticipantsOfType(_, classOf[ItPlatform]))
  def businessActorsWithKnowledgeOf(
      technology: Technology
  ): List[BusinessActor] =
    relationships(technology, classOf[Knowledge], classOf[BusinessActor])
      .flatMap(relationshipParticipantsOfType(_, classOf[BusinessActor]))

  val technologiesWithKnowledge: List[Technology] =
    relationshipsWithType(classOf[Knowledge])
      .flatMap(relationshipParticipantsOfType(_, classOf[Technology]))

  val technologiesWithoutKnowledge
      : List[TechnologyRadarRiskAssessment[Technology]] =
    technologies
      .filterNot(technologiesWithKnowledge.toSet)
      .map(TechnologyWithoutKnowledgeRiskAssessment)

  val containersWithToleratedTechnologies
      : List[TechnologyRadarRiskAssessment[ItContainer]] =
    technologies
      .filter(_.isToBeTolerated)
      .flatMap(containersImplementedWith)
      .map(ElementWithToleratedTechnologyAssessment(_))

  val containersWithInvestedTechnologies
      : List[TechnologyRadarRiskAssessment[ItContainer]] =
    technologies
      .filter(_.isToBeInvestedIn)
      .flatMap(containersImplementedWith)
      .map(ElementWithInvestedTechnologyAssessment(_))

  val containersWithMigratedTechnologies
      : List[TechnologyRadarRiskAssessment[ItContainer]] =
    technologies
      .filter(_.isToBeMigrated)
      .flatMap(containersImplementedWith)
      .map(ElementWithMigratedTechnologyAssessment(_))

  val containersWithEliminatedTechnologies
      : List[TechnologyRadarRiskAssessment[ItContainer]] =
    technologies
      .filter(_.isToBeEliminated)
      .flatMap(containersImplementedWith)
      .map(ElementWithEliminatedTechnologyAssessment(_))

}

trait TechnologyRadarRiskAssessment[Subject <: Element] {
  def element: Subject
  def severity: Severity
}

case class TechnologyWithoutKnowledgeRiskAssessment(element: Technology)
    extends TechnologyRadarRiskAssessment[Technology] {
  val severity: Severity = Red
}
case class ElementWithEliminatedTechnologyAssessment[
    ElementType <: Element with CanBeImplementedByTechnologies
](
    element: ElementType
) extends TechnologyRadarRiskAssessment[
      ElementType
    ] {
  val severity: Severity = Red
}
case class ElementWithMigratedTechnologyAssessment[
    ElementType <: Element with CanBeImplementedByTechnologies
](
    element: ElementType
) extends TechnologyRadarRiskAssessment[
      ElementType
    ] {
  val severity: Severity = Amber
}
case class ElementWithToleratedTechnologyAssessment[
    ElementType <: Element with CanBeImplementedByTechnologies
](
    element: ElementType
) extends TechnologyRadarRiskAssessment[
      ElementType
    ] {
  val severity: Severity = Amber
}
case class ElementWithInvestedTechnologyAssessment[
    ElementType <: Element with CanBeImplementedByTechnologies
](
    element: ElementType
) extends TechnologyRadarRiskAssessment[
      ElementType
    ] {
  val severity: Severity = Green
}

case class TechnologyRadarCompiler(
    view: TechnologyRadar,
    source: HasTechnologyRadars
) extends ViewCompiler[
      TechnologyRadar,
      CompiledTechnologyRadar,
      HasTechnologyRadars
    ] {
  def compile: CompiledTechnologyRadar =
    CompiledTechnologyRadar(
      view,
      viewTitle,
      "Technology Radar",
      viewComponents(
        technologies ++ containers ++ systems ++ platforms ++ businessActors ++ implementingTechnologies ++ composingContainers ++ composingSystems ++ knowingBusinessActors
      )
    )

  private def technologies: List[Technology] = source.technologies

  private def containers: Set[ItContainer] = implementingTechnologies.flatMap(
    source.relationshipParticipantsOfType(_, classOf[ItContainer])
  )

  private def implementingTechnologies: Set[Implementation] = technologies
    .flatMap(it =>
      source.relationships(it, classOf[Implementation], classOf[ItContainer])
    )
    .map(_.asInstanceOf[Implementation])
    .toSet

  private def composingContainers: Set[Composition] = containers
    .flatMap(it =>
      source.relationships(it, classOf[Composition], classOf[ItSystem])
    )
    .map(_.asInstanceOf[Composition])

  private def systems: Set[ItSystem] = composingContainers.flatMap(
    source.relationshipParticipantsOfType(_, classOf[ItSystem])
  )

  private def composingSystems: Set[Composition] = systems.flatMap(it =>
    source
      .relationships(it, classOf[Composition], classOf[ItPlatform])
      .map(_.asInstanceOf[Composition])
  )

  private def knowingBusinessActors: List[Knowledge] =
    technologies.flatMap(it =>
      source
        .relationships(it, classOf[Knowledge], classOf[BusinessActor])
        .map(_.asInstanceOf[Knowledge])
    )

  private def businessActors: List[BusinessActor] =
    knowingBusinessActors.flatMap(
      source.relationshipParticipantsOfType(_, classOf[BusinessActor])
    )

  private def platforms: Set[ItPlatform] = composingSystems.flatMap(
    source.relationshipParticipantsOfType(_, classOf[ItPlatform])
  )
}
