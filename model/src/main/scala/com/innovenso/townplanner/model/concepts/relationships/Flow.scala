package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
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
  def modelComponent: ModelComponentType

  def uses(target: CanBeFlowTarget, title: String): Relationship =
    flowsTo(target, title)

  def flowsTo(
      target: CanBeFlowTarget,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Flow(source = modelComponent.key, target = target.key, title = title)
    )

  def uses(target: CanBeFlowTarget): Relationship =
    flowsTo(target, "uses")
}

trait CanConfigureFlowTarget[ModelComponentType <: CanBeFlowTarget] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isUsedBy(target: CanBeFlowSource, title: String): Relationship =
    flowsFrom(target, title)

  def flowsFrom(
      target: CanBeFlowSource,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Flow(source = target.key, target = modelComponent.key, title = title)
    )

  def isUsedBy(target: CanBeFlowSource): Relationship =
    flowsFrom(target, "uses")
}
