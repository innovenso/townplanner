package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class Composition(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "is composed of",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "composition",
    "The composition relationship represents that an element consists of one or more other concepts."
  )
  val sourceTrait: Class[CanBeComposedOf] = classOf[CanBeComposedOf]
  val targetTrait: Class[CanCompose] = classOf[CanCompose]
  def withProperty(property: Property): Composition =
    copy(properties = this.properties + (property.key -> property))
}

trait CanBeComposedOf extends CanBeRelationshipSource
trait CanCompose extends CanBeRelationshipTarget

trait CanConfigureCompositionSource[ModelComponentType <: CanBeComposedOf] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isComposedOf(target: CanCompose): Relationship =
    isComposedOf(target, "")
  def isComposedOf(
      target: CanCompose,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Composition(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )
}

trait CanConfigureCompositionTarget[ModelComponentType <: CanCompose] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def composes(target: CanBeComposedOf): Relationship =
    composes(target, "")
  def composes(
      target: CanBeComposedOf,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Composition(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
