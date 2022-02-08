package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class Implementation(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "is implemented by",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "implementation",
    "An implementation relationship represents that one element is partly, or completely implemented by another element. A system can be implemented by a technology or by an infrastructure component for example."
  )
  val sourceTrait: Class[CanBeImplemented] = classOf[CanBeImplemented]
  val targetTrait: Class[CanImplement] = classOf[CanImplement]
  def withProperty(property: Property): Implementation =
    copy(properties = this.properties + (property.key -> property))
}

trait CanBeImplemented extends CanBeRelationshipSource
trait CanImplement extends CanBeRelationshipTarget

trait CanConfigureImplementationSource[ModelComponentType <: CanImplement] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def implements(target: CanBeImplemented): Relationship =
    implements(target, "implements")
  def implements(
      target: CanBeImplemented,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Implementation(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )
}

trait CanConfigureImplementationTarget[ModelComponentType <: CanBeImplemented] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isImplementedBy(target: CanImplement): Relationship =
    isImplementedBy(target, "uses")
  def isImplementedBy(
      target: CanImplement,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Implementation(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
