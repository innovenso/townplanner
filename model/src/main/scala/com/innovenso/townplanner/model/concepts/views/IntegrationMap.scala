package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  Association,
  CanAddRelationships,
  HasRelationships,
  Serving
}
import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.language._
import com.innovenso.townplanner.model.meta._

case class IntegrationMap(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String = "Integration Map",
    pointInTime: ADay = Today,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends View {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Integration Map"
  )
  val layer: Layer = ApplicationLayer

  def withProperty(property: Property): IntegrationMap =
    copy(properties = this.properties + (property.key -> property))

}

trait HasIntegrationMaps
    extends HasViews
    with HasItSystems
    with HasItSystemIntegrations {
  def integrationMaps: List[CompiledIntegrationMap] = components(
    classOf[IntegrationMap]
  ).map(view => IntegrationMapCompiler(view, this).compile)
  def integrationMap(key: Key): Option[CompiledIntegrationMap] =
    component(key, classOf[IntegrationMap]).map(
      IntegrationMapCompiler(_, this).compile
    )

}

trait CanAddIntegrationMaps extends CanAddProperties with CanAddRelationships {
  def needs(
      integrationMap: IntegrationMap
  ): IntegrationMap =
    has(integrationMap)
}

case class CompiledIntegrationMap(
    view: IntegrationMap,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[IntegrationMap]
    with HasRelationships
    with HasItSystems
    with HasItSystemIntegrations {}

case class IntegrationMapCompiler(
    view: IntegrationMap,
    source: HasIntegrationMaps
) extends ViewCompiler[
      IntegrationMap,
      CompiledIntegrationMap,
      HasIntegrationMaps
    ] {
  def compile: CompiledIntegrationMap =
    CompiledIntegrationMap(
      view,
      viewComponents(
        systems ++ relationships
      )
    )

  private def integrations: Iterable[ItSystemIntegration] =
    source.systemIntegrations.filter(visible)

  private def systems: Iterable[ItSystem] =
    integrations.flatMap(source.systemIntegrationParticipants).toSet

  private def relationships: Iterable[Association] = integrations
    .map(it =>
      Association(source = it.source, target = it.target, title = it.title)
    )
}
