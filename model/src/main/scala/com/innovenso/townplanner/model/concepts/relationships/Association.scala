package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class Association(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "is associated with",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "association",
    "An association relationship represents an unspecified relationship, or one that is not represented by another relationship."
  )
  val sourceTrait: Class[CanBeAssociated] = classOf[CanBeAssociated]
  val targetTrait: Class[CanBeAssociated] = classOf[CanBeAssociated]
  def withProperty(property: Property): Association =
    copy(properties = this.properties + (property.key -> property))
}

trait CanBeAssociated
    extends CanBeRelationshipSource
    with CanBeRelationshipTarget

trait CanConfigureAssociations[ModelComponentType <: CanBeAssociated] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isAssociatedWith(target: CanBeAssociated): Relationship =
    isAssociatedWith(target, "")
  def isAssociatedWith(
      target: CanBeAssociated,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Association(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )
}
