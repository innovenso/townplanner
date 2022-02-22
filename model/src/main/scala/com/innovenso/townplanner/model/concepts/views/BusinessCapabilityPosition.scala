package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  BusinessCapability,
  Enterprise,
  HasBusinessActors,
  HasBusinessCapabilities,
  HasEnterprises,
  HasItContainers,
  HasItSystems,
  ItSystem
}
import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAddRelationships,
  Flow,
  HasRelationships,
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

case class BusinessCapabilityPosition(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    forCapability: Key,
    title: String = "Business Capability",
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends View {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Business Capability"
  )
  val layer: Layer = StrategyLayer
  val pointInTime: ADay = Today

  def withProperty(property: Property): BusinessCapabilityPosition =
    copy(properties = this.properties + (property.key -> property))
}

trait HasBusinessCapabilityPositions
    extends HasViews
    with HasBusinessCapabilities
    with HasEnterprises {
  def businessCapabilityPositions: List[CompiledBusinessCapabilityPosition] =
    components(
      classOf[BusinessCapabilityPosition]
    ).map(view => BusinessCapabilityPositionCompiler(view, this).compile)
  def businessCapabilityPosition(
      key: Key
  ): Option[CompiledBusinessCapabilityPosition] =
    component(key, classOf[BusinessCapabilityPosition]).map(
      BusinessCapabilityPositionCompiler(_, this).compile
    )

}

trait CanAddBusinessCapabilityPositions
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      businessCapabilityPosition: BusinessCapabilityPosition
  ): BusinessCapabilityPosition =
    has(businessCapabilityPosition)
}

case class CompiledBusinessCapabilityPosition(
    view: BusinessCapabilityPosition,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[BusinessCapabilityPosition]
    with HasRelationships
    with HasBusinessCapabilities
    with HasEnterprises {
  def enterprise: Option[Enterprise] = enterprises.headOption
  def capability: Option[BusinessCapability] = businessCapability(
    view.forCapability
  )
}

case class BusinessCapabilityPositionCompiler(
    view: BusinessCapabilityPosition,
    source: HasBusinessCapabilities
) extends ViewCompiler[
      BusinessCapabilityPosition,
      CompiledBusinessCapabilityPosition,
      HasBusinessCapabilities
    ] {
  def compile: CompiledBusinessCapabilityPosition =
    CompiledBusinessCapabilityPosition(
      view,
      viewComponents(
        enterprise.toList ++ capabilities ++ servingCapabilities ++ servingEnterprises
      )
    )

  private def enterprise: Option[Enterprise] =
    capabilities
      .flatMap(c => source.directDependenciesOfType(c, classOf[Enterprise]))
      .headOption

  private def capabilities: Iterable[BusinessCapability] = source
    .businessCapability(view.forCapability)
    .map(source.businessCapabilityHierarchy)
    .getOrElse(Set())

  private def servingCapabilities: Iterable[Serving] = capabilities
    .flatMap(c =>
      source.relationships(c, classOf[Serving], classOf[BusinessCapability])
    )
    .map(_.asInstanceOf[Serving])

  private def servingEnterprises: Iterable[Serving] = capabilities
    .flatMap(c =>
      source.relationships(c, classOf[Serving], classOf[Enterprise])
    )
    .map(_.asInstanceOf[Serving])
}
