package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  BusinessCapability,
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
  Serving
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  HasViews,
  ModelComponent,
  TimelessView,
  ViewCompiler
}
import com.innovenso.townplanner.model.meta.{
  Key,
  Layer,
  ModelComponentType,
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
        technologies ++ containers ++ systems ++ platforms ++ implementingTechnologies ++ composingContainers ++ composingSystems
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

  private def platforms: Set[ItPlatform] = composingSystems.flatMap(
    source.relationshipParticipantsOfType(_, classOf[ItPlatform])
  )
}
