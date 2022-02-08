package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  HasArchitectureVerdict,
  HasDescription,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  CanBeAssociated,
  CanBeComposedOf,
  CanBeFlowSource,
  CanBeFlowTarget,
  CanBeTriggered,
  CanRealize,
  CanTrigger,
  HasRelationships,
  Realization
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class ItPlatform(
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
    with CanBeComposedOf {
  val layer: Layer = ApplicationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "IT Platform"
  )

  def withProperty(property: Property): ItPlatform =
    copy(properties = this.properties + (property.key -> property))
}

trait HasItPlatforms extends HasModelComponents with HasRelationships {
  def platforms: List[ItPlatform] = components(classOf[ItPlatform])
  def platform(key: Key): Option[ItPlatform] =
    component(key, classOf[ItPlatform])
  def realizedArchitectureBuildingBlocks(
      itPlatform: ItPlatform
  ): Set[ArchitectureBuildingBlock] = directOutgoingDependencies(
    itPlatform,
    classOf[Realization],
    classOf[ArchitectureBuildingBlock]
  )
  def realizingPlatforms(
      architectureBuildingBlock: ArchitectureBuildingBlock
  ): Set[ItPlatform] = directIncomingDependencies(
    architectureBuildingBlock,
    classOf[Realization],
    classOf[ItPlatform]
  )
}
