package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  HasBusinessActors,
  HasItContainers,
  HasItSystemIntegrations,
  HasItSystems,
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
  Implementation,
  Relationship
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  ModelComponent,
  View,
  ViewCompiler
}
import com.innovenso.townplanner.model.meta.{
  ADay,
  Key,
  ModelComponentType,
  SortKey,
  Today
}

case class SystemIntegrationView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    forSystemIntegration: Key,
    title: String = "",
    pointInTime: ADay = Today,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends View {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "System Integration View"
  )

  def withProperty(property: Property): SystemIntegrationView =
    copy(properties = this.properties + (property.key -> property))

}

trait HasSystemIntegrationViews
    extends HasItSystems
    with HasItSystemIntegrations
    with HasRelationships {
  def systemIntegrationViews: List[CompiledSystemIntegrationView] = components(
    classOf[SystemIntegrationView]
  ).map(view => SystemIntegrationViewCompiler(view, this).compile)
  def systemIntegrationView(key: Key): Option[CompiledSystemIntegrationView] =
    component(key, classOf[SystemIntegrationView]).map(
      SystemIntegrationViewCompiler(_, this).compile
    )
}

trait CanAddSystemIntegrationViews
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      systemIntegrationView: SystemIntegrationView
  ): SystemIntegrationView =
    has(systemIntegrationView)
}

case class CompiledSystemIntegrationView(
    view: SystemIntegrationView,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[SystemIntegrationView]
    with HasItSystems
    with HasItSystemIntegrations
    with HasRelationships

case class SystemIntegrationViewCompiler(
    view: SystemIntegrationView,
    source: HasSystemIntegrationViews
) extends ViewCompiler[
      SystemIntegrationView,
      CompiledSystemIntegrationView,
      HasSystemIntegrationViews
    ] {
  def compile: CompiledSystemIntegrationView =
    CompiledSystemIntegrationView(
      view,
      viewComponents(
        integration.toSet ++ systems ++ implementers ++ relationships
      )
    )

  def integration: Option[ItSystemIntegration] =
    source.systemIntegration(view.forSystemIntegration)

  def systems: Set[ItSystem] = integration
    .map(it => source.system(it.source).toSet ++ source.system(it.target).toSet)
    .getOrElse(Set())

  def relationships: Set[Relationship] =
    implementationRelationships ++ associationRelationships

  def implementationRelationships: Set[Relationship] = integration
    .map(it =>
      source.relationships(it, classOf[Implementation], classOf[ItSystem])
    )
    .getOrElse(Set())
    .toSet

  def associationRelationships: Set[Relationship] = integration
    .map(it =>
      Set(
        Association(source = it.source, target = it.key),
        Association(source = it.target, target = it.key)
      )
    )
    .getOrElse(Nil)
    .toSet

  def implementers: Set[ItSystem] = integration
    .map(it =>
      source
        .relationships(it, classOf[Implementation], classOf[ItSystem])
        .map(r => source.system(r.other(it.key).get))
        .filter(_.nonEmpty)
        .map(_.get)
        .toSet
    )
    .getOrElse(Set())

}
