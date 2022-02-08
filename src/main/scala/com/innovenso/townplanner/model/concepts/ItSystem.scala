package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  HasArchitectureVerdict,
  HasDescription,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class ItSystem(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDescription
    with HasArchitectureVerdict
    with CanBeFlowSource
    with CanBeFlowTarget
    with CanTrigger
    with CanBeTriggered
    with CanRealize
    with CanBeAssociated
    with CanCompose
    with CanBeComposedOf {
  val layer: Layer = ApplicationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "IT System"
  )

  def withProperty(property: Property): ItSystem =
    copy(properties = this.properties + (property.key -> property))
}

trait HasItSystems extends HasModelComponents with HasRelationships {
  def systems: List[ItSystem] = components(classOf[ItSystem])
  def system(key: Key): Option[ItSystem] = component(key, classOf[ItSystem])
  def platformSystems(itPlatform: ItPlatform): Set[ItSystem] =
    directOutgoingDependencies(
      itPlatform,
      classOf[Composition],
      classOf[ItSystem]
    )
  def systemPlatform(itSystem: ItSystem): Option[ItPlatform] =
    directIncomingDependencies(
      itSystem,
      classOf[Composition],
      classOf[ItPlatform]
    ).headOption

  def realizedArchitectureBuildingBlocks(
      itSystem: ItSystem
  ): Set[ArchitectureBuildingBlock] = directOutgoingDependencies(
    itSystem,
    classOf[Realization],
    classOf[ArchitectureBuildingBlock]
  )
  def realizingSystems(
      architectureBuildingBlock: ArchitectureBuildingBlock
  ): Set[ItSystem] = directIncomingDependencies(
    architectureBuildingBlock,
    classOf[Realization],
    classOf[ItSystem]
  )
}
