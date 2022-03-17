package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class Knowledge(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "has knowledge of",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "knowledge",
    "A knowledge relationship represents that one element knows about or has knowledge or expertise about another element. An individual or a team can have knowledge/expertise about a technology or about a system for example."
  )
  val sourceTrait: Class[CanKnow] = classOf[CanKnow]
  val targetTrait: Class[CanBeKnown] = classOf[CanBeKnown]
  def withProperty(property: Property): Knowledge =
    copy(properties = this.properties + (property.key -> property))
}

trait CanBeKnown extends CanBeRelationshipTarget
trait CanKnow extends CanBeRelationshipSource

trait CanConfigureKnowledgeSource[ModelComponentType <: CanKnow] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def hasKnowledgeOf(target: CanBeKnown): Relationship =
    hasKnowledgeOf(target, "has knowledge of")
  def hasKnowledgeOf(
      target: CanBeKnown,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Knowledge(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )
}

trait CanConfigureKnowledgeTarget[ModelComponentType <: CanBeKnown] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isKnownBy(target: CanKnow): Relationship =
    isKnownBy(target, "is known by")
  def isKnownBy(
      target: CanKnow,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Knowledge(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
