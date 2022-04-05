package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.{CanAddProperties, Property}
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
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def isTriggering(target: CanBeTriggered, title: String = "triggers"): RelationshipConfigurer =
    RelationshipConfigurer(triggers(target, title), propertyAdder, relationshipAdder)

  def triggers(
      target: CanBeTriggered,
      title: String = "triggers"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Trigger(source = modelComponent.key, target = target.key, title = title)
    )
}

trait CanConfigureTriggerTarget[ModelComponentType <: CanBeTriggered] {
  def relationshipAdder: CanAddRelationships
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def hasTrigger(target: CanTrigger, title: String = "triggers"): RelationshipConfigurer =
    RelationshipConfigurer(isTriggeredBy(target, title), propertyAdder, relationshipAdder)

  def isTriggeredBy(
      target: CanTrigger,
      title: String = "triggers"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Trigger(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
