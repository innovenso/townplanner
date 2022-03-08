package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  ArchitectureBuildingBlock,
  BusinessCapability,
  Enterprise,
  HasArchitectureBuildingBlocks,
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
  Realization,
  Serving
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
  Key,
  Layer,
  ModelComponentType,
  SortKey,
  StrategyLayer,
  Today
}

case class ArchitectureBuildingBlockRealizationView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    forBuildingBlock: Key,
    includeContainers: Boolean = true,
    title: String = "Architecture Building Block Realization",
    pointInTime: ADay = Today,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends View {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Architecture Building Block Realization"
  )
  val layer: Layer = ApplicationLayer

  def withProperty(
      property: Property
  ): ArchitectureBuildingBlockRealizationView =
    copy(properties = this.properties + (property.key -> property))
}

object ArchitectureBuildingBlockRealizationView {
  def apply(
      forBuildingBlock: ArchitectureBuildingBlock,
      pointInTime: ADay
  ) = new ArchitectureBuildingBlockRealizationView(
    forBuildingBlock = forBuildingBlock.key,
    pointInTime = pointInTime
  )

  def apply(
      forBuildingBlock: ArchitectureBuildingBlock,
      pointInTime: ADay,
      includeContainers: Boolean
  ) = new ArchitectureBuildingBlockRealizationView(
    forBuildingBlock = forBuildingBlock.key,
    pointInTime = pointInTime,
    includeContainers = includeContainers
  )

  def apply(
      forBuildingBlock: ArchitectureBuildingBlock
  ) = new ArchitectureBuildingBlockRealizationView(
    forBuildingBlock = forBuildingBlock.key
  )

  def apply(
      forBuildingBlock: ArchitectureBuildingBlock,
      includeContainers: Boolean
  ) = new ArchitectureBuildingBlockRealizationView(
    forBuildingBlock = forBuildingBlock.key,
    includeContainers = includeContainers
  )
}

trait HasArchitectureBuildingBlockRealizationViews
    extends HasViews
    with HasBusinessCapabilities
    with HasArchitectureBuildingBlocks
    with HasItPlatforms
    with HasItSystems
    with HasItContainers
    with HasTechnologies
    with HasEnterprises {
  def architectureBuildingBlockRealizationViews
      : List[CompiledArchitectureBuildingBlockRealizationView] =
    components(
      classOf[ArchitectureBuildingBlockRealizationView]
    ).map(view =>
      ArchitectureBuildingBlockRealizationViewCompiler(view, this).compile
    )
  def architectureBuildingBlockRealizationView(
      key: Key
  ): Option[CompiledArchitectureBuildingBlockRealizationView] =
    component(key, classOf[ArchitectureBuildingBlockRealizationView]).map(
      ArchitectureBuildingBlockRealizationViewCompiler(_, this).compile
    )

}

trait CanAddArchitectureBuildingBlockRealizationViews
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      architectureBuildingBlockRealizationView: ArchitectureBuildingBlockRealizationView
  ): ArchitectureBuildingBlockRealizationView =
    has(architectureBuildingBlockRealizationView)
}

case class CompiledArchitectureBuildingBlockRealizationView(
    view: ArchitectureBuildingBlockRealizationView,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[ArchitectureBuildingBlockRealizationView]
    with HasRelationships
    with HasBusinessCapabilities
    with HasEnterprises
    with HasArchitectureBuildingBlocks
    with HasItPlatforms
    with HasItSystems
    with HasItContainers
    with HasTechnologies {
  def enterprise: Option[Enterprise] = enterprises.headOption
  def buildingBlock: Option[ArchitectureBuildingBlock] = {
    architectureBuildingBlock(
      view.forBuildingBlock
    )
  }
}

case class ArchitectureBuildingBlockRealizationViewCompiler(
    view: ArchitectureBuildingBlockRealizationView,
    source: HasArchitectureBuildingBlockRealizationViews
) extends ViewCompiler[
      ArchitectureBuildingBlockRealizationView,
      CompiledArchitectureBuildingBlockRealizationView,
      HasArchitectureBuildingBlockRealizationViews
    ] {
  def compile: CompiledArchitectureBuildingBlockRealizationView =
    CompiledArchitectureBuildingBlockRealizationView(
      view,
      viewTitle,
      groupTitle(view.forBuildingBlock),
      viewComponents(
        enterprises ++ capabilities ++ buildingBlocks ++ platforms ++ systems ++ containers ++ servingCapabilities ++ servingEnterprises ++ realizingBuildingBlocks ++ realizingPlatforms ++ realizingSystems ++ composingSystems ++ composingContainers ++ technologies ++ implementingTechnologies
      )
    )

  def buildingBlocks: Set[ArchitectureBuildingBlock] =
    source.architectureBuildingBlock(view.forBuildingBlock).toSet

  def capabilities: Set[BusinessCapability] = buildingBlocks
    .flatMap(source.realizedBusinessCapabilities)
    .flatMap(source.businessCapabilityParentHierarchy)

  def enterprises: Set[Enterprise] = capabilities.flatMap(source.enterprise)

  def platforms: Set[ItPlatform] =
    buildingBlocks.flatMap(source.realizingPlatforms)

  def directlyRealizingSystems: Set[ItSystem] =
    buildingBlocks.flatMap(source.realizingSystems)

  def platformRealizingSystems: Set[ItSystem] =
    platforms.flatMap(source.platformSystems)

  def systems: Set[ItSystem] =
    directlyRealizingSystems ++ platformRealizingSystems

  def containers: Set[ItContainer] =
    if (view.includeContainers) systems.flatMap(source.containers) else Set()

  def technologies: Set[Technology] = containers.flatMap(source.technologies(_))

  def servingCapabilities: Set[Serving] = capabilities
    .flatMap(c =>
      source.relationships(c, classOf[Serving], classOf[BusinessCapability])
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[BusinessCapability])
        .forall(capabilities.contains(_))
    )
    .map(_.asInstanceOf[Serving])

  def servingEnterprises: Set[Serving] = capabilities
    .flatMap(c =>
      source.relationships(c, classOf[Serving], classOf[Enterprise])
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[Enterprise])
        .forall(enterprises.contains(_))
    )
    .map(_.asInstanceOf[Serving])

  def realizingBuildingBlocks: Set[Realization] = buildingBlocks
    .flatMap(bb =>
      source
        .relationships(bb, classOf[Realization], classOf[BusinessCapability])
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[BusinessCapability])
        .forall(capabilities.contains(_))
    )
    .map(_.asInstanceOf[Realization])

  def realizingPlatforms: Set[Realization] = platforms
    .flatMap(bb =>
      source
        .relationships(
          bb,
          classOf[Realization],
          classOf[ArchitectureBuildingBlock]
        )
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[ArchitectureBuildingBlock])
        .forall(buildingBlocks.contains(_))
    )
    .map(_.asInstanceOf[Realization])

  def realizingSystems: Set[Realization] = systems
    .flatMap(bb =>
      source
        .relationships(
          bb,
          classOf[Realization],
          classOf[ArchitectureBuildingBlock]
        )
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[ArchitectureBuildingBlock])
        .forall(buildingBlocks.contains(_))
    )
    .map(_.asInstanceOf[Realization])

  def composingSystems: Set[Composition] = systems
    .flatMap(bb =>
      source
        .relationships(bb, classOf[Composition], classOf[ItPlatform])
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[ItPlatform])
        .forall(platforms.contains(_))
    )
    .map(_.asInstanceOf[Composition])

  def composingContainers: Set[Composition] = containers
    .flatMap(it =>
      source
        .relationships(it, classOf[Composition], classOf[ItSystem])
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[ItSystem])
        .forall(systems.contains(_))
    )
    .map(_.asInstanceOf[Composition])

  def implementingTechnologies: Set[Implementation] = technologies
    .flatMap(it =>
      source.relationships(it, classOf[Implementation], classOf[ItContainer])
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[ItContainer])
        .forall(containers.contains(_))
    )
    .map(_.asInstanceOf[Implementation])
}
