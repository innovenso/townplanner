package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  Actor,
  HasBusinessActors,
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
  Flow,
  HasRelationships,
  Implementation
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  Element,
  HasViews,
  ModelComponent,
  View,
  ViewCompiler
}
import com.innovenso.townplanner.model.meta.{
  ADay,
  ApplicationLayer,
  Key,
  Layer,
  ModelComponentType,
  SortKey,
  Today
}

case class PlatformSystemView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    forPlatform: Key,
    baseTitle: String = "Platform System View",
    withContainers: Boolean = false,
    pointInTime: ADay = Today,
    withStepCounter: Boolean = true,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends View {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Platform System View"
  )
  val layer: Layer = ApplicationLayer

  def withProperty(property: Property): PlatformSystemView =
    copy(properties = this.properties + (property.key -> property))

  val title: String =
    if (withContainers) s"$baseTitle - Low Level"
    else s"$baseTitle - High Level"
}

object PlatformSystemView {
  def apply(
      forPlatform: ItPlatform,
      pointInTime: ADay
  ) = new PlatformSystemView(
    forPlatform = forPlatform.key,
    pointInTime = pointInTime
  )

  def apply(
      forPlatform: ItPlatform
  ) = new PlatformSystemView(forPlatform = forPlatform.key)

  def apply(
      forPlatform: ItPlatform,
      withContainers: Boolean
  ) = new PlatformSystemView(
    forPlatform = forPlatform.key,
    withContainers = withContainers
  )
}

trait HasPlatformSystemViews
    extends HasViews
    with HasItPlatforms
    with HasItSystems
    with HasItContainers
    with HasRelationships
    with HasBusinessActors
    with HasTechnologies {
  def platformSystemViews: List[CompiledPlatformSystemView] = components(
    classOf[PlatformSystemView]
  ).map(view => PlatformSystemViewCompiler(view, this).compile)
  def platformSystemView(key: Key): Option[CompiledPlatformSystemView] =
    component(key, classOf[PlatformSystemView]).map(
      PlatformSystemViewCompiler(_, this).compile
    )
}

trait CanAddPlatformSystemViews
    extends CanAddProperties
    with CanAddRelationships {
  def needs(platformSystemView: PlatformSystemView): PlatformSystemView =
    has(platformSystemView)
}

case class CompiledPlatformSystemView(
    view: PlatformSystemView,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[PlatformSystemView]
    with HasItPlatforms
    with HasItSystems
    with HasItContainers
    with HasRelationships
    with HasBusinessActors
    with HasTechnologies {
  def otherSystems: List[ItSystem] = systems.filterNot(centralSystems.toSet)

  def centralPlatform: Option[ItPlatform] = platform(view.forPlatform)

  def centralSystems: List[ItSystem] =
    centralPlatform.map(platformSystems).getOrElse(Nil)

  def flows: List[Flow] = relationshipsWithType(classOf[Flow])
}

case class PlatformSystemViewCompiler(
    view: PlatformSystemView,
    source: HasPlatformSystemViews
) extends ViewCompiler[
      PlatformSystemView,
      CompiledPlatformSystemView,
      HasPlatformSystemViews
    ] {
  def compile: CompiledPlatformSystemView =
    CompiledPlatformSystemView(
      view,
      viewTitle,
      groupTitle(view.forPlatform),
      viewComponents(
        platform.toList ++ systems ++ containers ++ actors ++ flows ++ technologies ++ implementingTechnologies ++ compositions
      )
    )

  private def flows: Set[Flow] =
    allFlows.filter(flow =>
      isInDiagram(flow.source) && isInDiagram(flow.target)
    )

  private def isInDiagram(key: Key): Boolean =
    visible(key) && (platform.toList ++ systems ++ containers ++ actors)
      .exists(_.key == key)

  private def actors: Set[Actor] = allFlows
    .flatMap(relationship => Set(relationship.source, relationship.target))
    .map(key => source.businessActor(key))
    .filter(_.nonEmpty)
    .map(_.get)
    .filter(_.isInstanceOf[Actor])
    .map(_.asInstanceOf[Actor])

  private def containers: Set[ItContainer] = if (view.withContainers)
    centralSystems.flatMap(source.containers)
  else Set()

  def technologies: Set[Technology] = containers.flatMap(source.technologies(_))

  private def systems: Set[ItSystem] =
    centralSystems ++ directlyDependentSystems ++ childDependentSystems

  private def platform: Option[ItPlatform] = source.platform(view.forPlatform)

  private def centralSystems: Set[ItSystem] =
    platform.map(source.platformSystems).getOrElse(Nil).toSet

  private def directlyDependentSystems: Iterable[ItSystem] = {
    centralSystems
      .flatMap(source.directDependenciesOfType(_, classOf[ItSystem]))
      .filterNot(centralSystems)
  }

  private def childDependentSystems: Iterable[ItSystem] = if (
    view.withContainers
  )
    centralSystems
      .flatMap(source.containers)
      .flatMap(source.directDependenciesOfType(_, classOf[ItSystem]))
      .filterNot(centralSystems)
  else Set()

  private def allFlows: Set[Flow] = flowSources
    .flatMap(element =>
      source.relationships(element, classOf[Flow]).map(_.asInstanceOf[Flow])
    )

  private def flowSources: Set[Element] = centralSystems.flatMap(flowSources)

  private def flowSources(system: ItSystem): Set[Element] = if (
    view.withContainers && source.containers(system).nonEmpty
  ) source.containers(system).map(_.asInstanceOf[Element]).toSet
  else Set(system)

  private def implementingTechnologies: Set[Implementation] = technologies
    .flatMap(it =>
      source.relationships(it, classOf[Implementation], classOf[ItContainer])
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[ItContainer])
        .forall(containers.contains(_))
    )
    .map(_.asInstanceOf[Implementation])

  private def compositions: Set[Composition] =
    systemContainerCompositions ++ platformSystemCompositions

  private def systemContainerCompositions: Set[Composition] = if (
    view.withContainers
  )
    centralSystems.flatMap(element =>
      source
        .relationships(element, classOf[Composition])
        .filter(r =>
          r.other(element.key).exists(o => containers.exists(_.key == o))
        )
        .map(_.asInstanceOf[Composition])
    )
  else Set()

  private def platformSystemCompositions: Set[Composition] = platform.toList
    .flatMap(element =>
      source
        .relationships(element, classOf[Composition])
        .filter(r =>
          r.other(element.key).exists(o => centralSystems.exists(_.key == o))
        )
        .map(_.asInstanceOf[Composition])
    )
    .toSet
}
