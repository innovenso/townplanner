package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.{CanAddProperties, Property}
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
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def hasExpertiseOf(target: CanBeKnown, title: String = "has knowledge of"): RelationshipConfigurer =
    RelationshipConfigurer(hasKnowledgeOf(target, title), propertyAdder, relationshipAdder)

  def hasKnowledgeOf(
      target: CanBeKnown,
      title: String = "has knowledge of"
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
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def belongsToExpertiseOf(target: CanKnow, title: String = "has knowledge of"): RelationshipConfigurer =
    RelationshipConfigurer(isKnownBy(target, title), propertyAdder, relationshipAdder)

  def isKnownBy(
      target: CanKnow,
      title: String = "is known by"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Knowledge(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
