package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  CanConfigureArchitectureVerdict,
  CanConfigureDescription,
  HasArchitectureVerdict,
  HasDescription,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class ArchitectureBuildingBlock(
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
      classOf[Serving],
      classOf[Enterprise]
    ).headOption
}

case class ArchitectureBuildingBlockConfigurer(
    modelComponent: ArchitectureBuildingBlock,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[ArchitectureBuildingBlock]
    with CanConfigureArchitectureVerdict[ArchitectureBuildingBlock]
    with CanConfigureServingSource[ArchitectureBuildingBlock]
    with CanConfigureRealizationTarget[ArchitectureBuildingBlock]
    with CanConfigureRealizationSource[ArchitectureBuildingBlock]
    with CanConfigureFlowSource[ArchitectureBuildingBlock]
    with CanConfigureFlowTarget[ArchitectureBuildingBlock]
    with CanConfigureTriggerSource[ArchitectureBuildingBlock]
    with CanConfigureTriggerTarget[ArchitectureBuildingBlock]
    with CanConfigureAssociations[ArchitectureBuildingBlock] {
  def as(
      body: ArchitectureBuildingBlockConfigurer => Any
  ): ArchitectureBuildingBlock = {
    body.apply(this)
    propertyAdder.townPlan
      .architectureBuildingBlock(modelComponent.key)
      .get
  }
}

trait CanAddArchitectureBuildingBlocks
    extends CanAddProperties
    with CanAddRelationships {
  def describes(
      architectureBuildingBlock: ArchitectureBuildingBlock
  ): ArchitectureBuildingBlockConfigurer =
    ArchitectureBuildingBlockConfigurer(
      has(architectureBuildingBlock),
      this,
      this
    )
}
