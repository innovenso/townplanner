package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class Trigger(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "triggers",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "trigger",
    "The triggering relationship represents a temporal or causal relationship between elements."
  )
  val sourceTrait: Class[CanTrigger] = classOf[CanTrigger]
  val targetTrait: Class[CanBeTriggered] = classOf[CanBeTriggered]
  def withProperty(property: Property): Trigger =
    copy(properties = this.properties + (property.key -> property))
}

trait CanTrigger extends CanBeRelationshipSource
trait CanBeTriggered extends CanBeRelationshipTarget

trait CanConfigureTriggerSource[ModelComponentType <: CanTrigger] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def triggers(
      target: CanBeTriggered
  ): Relationship = triggers(target, "triggers")
  def triggers(
      target: CanBeTriggered,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Trigger(source = modelComponent.key, target = target.key, title = title)
    )
}

trait CanConfigureTriggerTarget[ModelComponentType <: CanBeTriggered] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isTriggeredBy(target: CanTrigger): Relationship =
    isTriggeredBy(target, "is triggered by")
  def isTriggeredBy(
      target: CanTrigger,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Trigger(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
