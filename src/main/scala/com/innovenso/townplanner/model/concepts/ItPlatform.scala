package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.{CanManipulateTownPlan, TownPlan}
import com.innovenso.townplanner.model.concepts.properties.{
  HasArchitectureVerdict,
  HasDocumentation,
  Property
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta.{
  ActiveStructure,
  ApplicationLayer,
  Aspect,
  Description,
  Key,
  Layer,
  ModelComponentType,
  SortKey,
  Title
}

import scala.util.Try

case class ItPlatform(
    key: Key,
    sortKey: SortKey,
    title: Title,
    description: Description,
    properties: Map[Key, Property]
) extends Element
    with HasDocumentation
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
    Realizes,
    classOf[ArchitectureBuildingBlock]
  )
  def realizingPlatforms(
      architectureBuildingBlock: ArchitectureBuildingBlock
  ): Set[ItPlatform] = directIncomingDependencies(
    architectureBuildingBlock,
    Realizes,
    classOf[ItPlatform]
  )

}

trait CanAddItPlatforms extends CanManipulateTownPlan {
  def withItPlatform(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None)
  ): Try[(TownPlan, ItPlatform)] = withNewModelComponent(
    ItPlatform(
      key = key,
      sortKey = sortKey,
      title = title,
      description = description,
      properties = Map.empty[Key, Property]
    )
  )
}
