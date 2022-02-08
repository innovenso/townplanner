package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class Influence(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "influences",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "influences",
    "The influences relationship represents that a element influences another element. For example, a business stakeholder can influence a decision."
  )
  val sourceTrait: Class[CanInfluence] = classOf[CanInfluence]
  val targetTrait: Class[CanBeInfluenced] = classOf[CanBeInfluenced]
  def withProperty(property: Property): Influence =
    copy(properties = this.properties + (property.key -> property))
}

trait CanInfluence extends CanBeRelationshipSource
trait CanBeInfluenced extends CanBeRelationshipTarget

trait CanConfigureInfluenceSource[ModelComponentType <: CanInfluence] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def influences(target: CanBeInfluenced): Relationship =
    influences(target, "influences")
  def influences(
      target: CanBeInfluenced,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Influence(source = modelComponent.key, target = target.key, title = title)
    )
}

trait CanConfigureInfluenceTarget[ModelComponentType <: CanBeInfluenced] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isInfluencedBy(target: CanInfluence): Relationship =
    isInfluencedBy(target, "influences")
  def isInfluencedBy(
      target: CanInfluence,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Influence(source = target.key, target = modelComponent.key, title = title)
    )
}
