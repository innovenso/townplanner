package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  HasDocumentation,
  Property
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class ItContainer(
    key: Key,
    sortKey: SortKey,
    title: Title,
    description: Description,
    properties: Map[Key, Property]
) extends Element
    with HasDocumentation
    with CanBeFlowSource
    with CanBeFlowTarget
    with CanTrigger
    with CanBeTriggered
    with CanBeAssociated
    with CanCompose {
  val layer: Layer = ApplicationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "IT Container"
  )

  def withProperty(property: Property): ItContainer =
    copy(properties = this.properties + (property.key -> property))
}

trait HasItContainers extends HasModelComponents with HasRelationships {
  def containers: List[ItContainer] = components(classOf[ItContainer])
  def container(key: Key): Option[ItContainer] =
    component(key, classOf[ItContainer])
  def containers(itSystem: ItSystem): Set[ItContainer] =
    directOutgoingDependencies(itSystem, IsComposedOf, classOf[ItContainer])
  def system(itContainer: ItContainer): Option[ItSystem] =
    directIncomingDependencies(
      itContainer,
      IsComposedOf,
      classOf[ItSystem]
    ).headOption

}
