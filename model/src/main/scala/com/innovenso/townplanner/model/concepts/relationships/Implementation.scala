package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.{CanAddProperties, Property}
import com.innovenso.townplanner.model.meta.Key

case class Implementation(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "implements",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "implementation",
    "An implementation relationship represents that one element is partly, or completely implemented by another element. A system can be implemented by a technology or by an infrastructure component for example."
  )
  val sourceTrait: Class[CanImplement] = classOf[CanImplement]
  val targetTrait: Class[CanBeImplemented] = classOf[CanBeImplemented]
  def withProperty(property: Property): Implementation =
    copy(properties = this.properties + (property.key -> property))
}

trait CanBeImplemented extends CanBeRelationshipTarget
trait CanImplement extends CanBeRelationshipSource

trait CanConfigureImplementationSource[ModelComponentType <: CanImplement] {
  def relationshipAdder: CanAddRelationships
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def isImplementing(target: CanBeImplemented, title: String = "implements"): RelationshipConfigurer =
    RelationshipConfigurer(implements(target, title), propertyAdder, relationshipAdder)
  def implements(
      target: CanBeImplemented,
      title: String = "implements"
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
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def isBeingImplementedBy(target: CanImplement, title: String = "implements"): RelationshipConfigurer =
    RelationshipConfigurer(isImplementedBy(target, title), propertyAdder, relationshipAdder)

  def isImplementedBy(
      target: CanImplement,
      title: String = "implements"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Implementation(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
