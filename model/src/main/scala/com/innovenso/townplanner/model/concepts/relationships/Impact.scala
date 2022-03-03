package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class CreateImpact(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "creates",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "creates",
    "The creates relationship represents that a element, such as a project or decision, adds another element to the architecture."
  )
  val sourceTrait: Class[CanImpact] = classOf[CanImpact]
  val targetTrait: Class[CanBeImpacted] = classOf[CanBeImpacted]
  def withProperty(property: Property): CreateImpact =
    copy(properties = this.properties + (property.key -> property))
}

trait CanImpact extends CanBeRelationshipSource
trait CanBeImpacted extends CanBeRelationshipTarget

case class RemoveImpact(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "removes",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "removes",
    "The removes relationship represents that a element, such as a project or decision, removes another element from the architecture."
  )
  val sourceTrait: Class[CanImpact] = classOf[CanImpact]
  val targetTrait: Class[CanBeImpacted] = classOf[CanBeImpacted]
  def withProperty(property: Property): RemoveImpact =
    copy(properties = this.properties + (property.key -> property))
}

case class ChangeImpact(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "changes",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "changes",
    "The changes relationship represents that a element, such as a project or decision, changes another element in the architecture."
  )
  val sourceTrait: Class[CanImpact] = classOf[CanImpact]
  val targetTrait: Class[CanBeImpacted] = classOf[CanBeImpacted]
  def withProperty(property: Property): ChangeImpact =
    copy(properties = this.properties + (property.key -> property))
}

case class KeepImpact(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "keeps",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "keeps",
    "The keeps relationship represents that a element, such as a project or decision, keeps another element in the architecture as it is."
  )
  val sourceTrait: Class[CanImpact] = classOf[CanImpact]
  val targetTrait: Class[CanBeImpacted] = classOf[CanBeImpacted]
  def withProperty(property: Property): KeepImpact =
    copy(properties = this.properties + (property.key -> property))
}

trait CanConfigureImpactSource[ModelComponentType <: CanImpact] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def creates(target: CanBeImpacted): Relationship =
    creates(target, "creates")

  def removes(target: CanBeImpacted): Relationship =
    removes(target, "removes")

  def removes(
      target: CanBeImpacted,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      RemoveImpact(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )

  def changes(target: CanBeImpacted): Relationship =
    changes(target, "changes")

  def changes(
      target: CanBeImpacted,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      ChangeImpact(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )

  def keeps(target: CanBeImpacted): Relationship =
    keeps(target, "keeps")

  def creates(
      target: CanBeImpacted,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      CreateImpact(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )

  def keeps(
      target: CanBeImpacted,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      KeepImpact(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )
}
