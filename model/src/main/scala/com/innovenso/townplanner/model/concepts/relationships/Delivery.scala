package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class Delivery(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "delivers",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "delivery",
    "The delivery relationship represents that an element such as an individual or a team is responsible for the delivery of another element."
  )
  val sourceTrait: Class[CanDeliver] = classOf[CanDeliver]
  val targetTrait: Class[CanBeDelivered] = classOf[CanBeDelivered]
  def withProperty(property: Property): Delivery =
    copy(properties = this.properties + (property.key -> property))
}

trait CanDeliver extends CanBeRelationshipSource
trait CanBeDelivered extends CanBeRelationshipTarget

trait CanConfigureDeliverySource[ModelComponentType <: CanDeliver] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def delivers(target: CanBeDelivered): Relationship =
    delivers(target, "delivers")
  def delivers(
      target: CanBeDelivered,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Delivery(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )
}

trait CanConfigureDeliveryTarget[ModelComponentType <: CanBeDelivered] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isDeliveredBy(target: CanDeliver): Relationship =
    isDeliveredBy(target, "delivers")
  def isDeliveredBy(
      target: CanDeliver,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Delivery(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
