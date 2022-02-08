package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class Serving(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "serves",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "serving",
    "The serving relationship represents that an element provides its functionality to another element."
  )
  val sourceTrait: Class[CanServe] = classOf[CanServe]
  val targetTrait: Class[CanBeServed] = classOf[CanBeServed]
  def withProperty(property: Property): Serving =
    copy(properties = this.properties + (property.key -> property))
}

trait CanServe extends CanBeRelationshipSource
trait CanBeServed extends CanBeRelationshipTarget

trait CanConfigureServingSource[ModelComponentType <: CanServe] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def serves(target: CanBeServed): Relationship =
    serves(target, "serves")
  def serves(
      target: CanBeServed,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Serving(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )
}

trait CanConfigureServingTarget[ModelComponentType <: CanBeServed] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isServedBy(target: CanServe): Relationship =
    isServedBy(target, "serves")
  def isServedBy(
      target: CanServe,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Serving(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
