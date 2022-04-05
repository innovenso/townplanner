package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.{CanAddProperties, Description, Property}
import com.innovenso.townplanner.model.meta.Key

case class Flow(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "uses",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "flow",
    "The flow relationship represents transfer from one element to another."
  )
  val sourceTrait: Class[CanBeFlowSource] = classOf[CanBeFlowSource]
  val targetTrait: Class[CanBeFlowTarget] = classOf[CanBeFlowTarget]

  def withProperty(property: Property): Flow =
    copy(properties = this.properties + (property.key -> property))
}

trait CanBeFlowSource extends CanBeRelationshipSource
trait CanBeFlowTarget extends CanBeRelationshipTarget

trait CanConfigureFlowSource[ModelComponentType <: CanBeFlowSource] {
  def relationshipAdder: CanAddRelationships
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def isUsing(target: CanBeFlowTarget): RelationshipConfigurer = isFlowingTo(target, "uses")

  def isFlowingTo(target: CanBeFlowTarget, title: String = ""): RelationshipConfigurer =
    RelationshipConfigurer(flowsTo(target, title), propertyAdder, relationshipAdder)

  def uses(target: CanBeFlowTarget, title: String = "uses"): Relationship =
    flowsTo(target, title)

  def flowsTo(
      target: CanBeFlowTarget,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Flow(source = modelComponent.key, target = target.key, title = title)
    )

}

trait CanConfigureFlowTarget[ModelComponentType <: CanBeFlowTarget] {
  def relationshipAdder: CanAddRelationships
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def isBeingUsedBy(target: CanBeFlowSource, title: String = "uses"): RelationshipConfigurer = isFlowingFrom(target, title)

  def isFlowingFrom(target: CanBeFlowSource, title: String = ""): RelationshipConfigurer =
    RelationshipConfigurer(flowsFrom(target, title), propertyAdder, relationshipAdder)

  def isUsedBy(target: CanBeFlowSource, title: String = "uses"): Relationship =
    flowsFrom(target, title)

  def flowsFrom(
      target: CanBeFlowSource,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Flow(source = target.key, target = modelComponent.key, title = title)
    )

}
