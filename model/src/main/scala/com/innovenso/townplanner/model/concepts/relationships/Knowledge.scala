package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  Property
}
import com.innovenso.townplanner.model.meta.Key

case class Knowledge(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "has knowledge of",
    level: KnowledgeLevel = Knowledgeable,
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

  def hasExpertiseOf(
      target: CanBeKnown,
      title: String = "has knowledge of",
      level: KnowledgeLevel = Knowledgeable
  ): RelationshipConfigurer =
    RelationshipConfigurer(
      hasKnowledgeOf(target, title, level),
      propertyAdder,
      relationshipAdder
    )

  def hasKnowledgeOf(
      target: CanBeKnown,
      title: String = "has knowledge of",
      level: KnowledgeLevel = Knowledgeable
  ): Relationship =
    relationshipAdder.hasRelationship(
      Knowledge(
        source = modelComponent.key,
        target = target.key,
        title = title,
        level = level
      )
    )
}

trait CanConfigureKnowledgeTarget[ModelComponentType <: CanBeKnown] {
  def relationshipAdder: CanAddRelationships
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def belongsToExpertiseOf(
      target: CanKnow,
      title: String = "has knowledge of",
      level: KnowledgeLevel = Knowledgeable
  ): RelationshipConfigurer =
    RelationshipConfigurer(
      isKnownBy(target, title, level),
      propertyAdder,
      relationshipAdder
    )

  def isKnownBy(
      target: CanKnow,
      title: String = "has knowledge of",
      level: KnowledgeLevel = Knowledgeable
  ): Relationship =
    relationshipAdder.hasRelationship(
      Knowledge(
        source = target.key,
        target = modelComponent.key,
        title = title,
        level = level
      )
    )
}

sealed trait KnowledgeLevel {
  def name: String
  def level: Int
}

case object NoKnowledge extends KnowledgeLevel {
  val name: String = "No knowledge"
  val level: Int = 0
}

case object Learner extends KnowledgeLevel {
  val name: String = "Learner"
  val level: Int = 1
}

case object Knowledgeable extends KnowledgeLevel {
  val name: String = "Knowledgeable"
  val level: Int = 2
}

case object HighlyKnowledgeable extends KnowledgeLevel {
  val name: String = "Highly Knowledgeable"
  val level: Int = 3
}

case object Expert extends KnowledgeLevel {
  val name: String = "Expert"
  val level: Int = 4
}
