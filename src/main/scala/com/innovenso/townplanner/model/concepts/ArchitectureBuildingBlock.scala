package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.{CanManipulateTownPlan, TownPlan}
import com.innovenso.townplanner.model.concepts.properties.{
  HasDocumentation,
  Property
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta.{
  ActiveStructure,
  ApplicationLayer,
  Aspect,
  Behavior,
  BusinessLayer,
  Description,
  Key,
  Layer,
  ModelComponentType,
  PassiveStructure,
  SortKey,
  StrategyLayer,
  Title
}

import scala.util.{Success, Try}

case class ArchitectureBuildingBlock(
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
    with CanRealize
    with CanBeRealized
    with CanServe
    with CanBeAssociated {
  val layer: Layer = ApplicationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Architecture Building Block"
  )

  def withProperty(property: Property): ArchitectureBuildingBlock =
    copy(properties = this.properties + (property.key -> property))
}

trait HasArchitectureBuildingBlocks
    extends HasModelComponents
    with HasBusinessCapabilities
    with HasRelationships {
  def architectureBuildingBlocks: List[ArchitectureBuildingBlock] = components(
    classOf[ArchitectureBuildingBlock]
  )
  def architectureBuildingBlock(key: Key): Option[ArchitectureBuildingBlock] =
    component(key, classOf[ArchitectureBuildingBlock])
  def enterprise(
      architectureBuildingBlock: ArchitectureBuildingBlock
  ): Option[Enterprise] =
    directOutgoingDependencies(
      architectureBuildingBlock,
      Serves,
      classOf[Enterprise]
    ).headOption
}

trait CanAddArchitectureBuildingBlocks
    extends CanManipulateTownPlan
    with CanAddRelationships {
  def withArchitectureBuildingBlock(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None)
  ): Try[(TownPlan, ArchitectureBuildingBlock)] = withNewModelComponent(
    ArchitectureBuildingBlock(
      key = key,
      sortKey = sortKey,
      title = title,
      description = description,
      properties = Map.empty[Key, Property]
    )
  )

  def withEnterpriseArchitectureBuildingBlock(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None),
      enterprise: Enterprise
  ): Try[(TownPlan, ArchitectureBuildingBlock)] =
    withArchitectureBuildingBlock(key, sortKey, title, description)
      .flatMap(tb =>
        withRelationship(
          title = Title(""),
          relationshipType = Serves,
          sourceKey = tb._2.key,
          targetKey = enterprise.key
        ).flatMap(tr => Success((tr._1, tb._2)))
      )

}
