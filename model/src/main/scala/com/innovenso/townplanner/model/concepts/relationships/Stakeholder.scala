package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  Property
}
import com.innovenso.townplanner.model.meta.Key

case class Stakeholder(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "is stakeholder for",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
  val relationshipType: RelationshipType = RelationshipType(
    "stakeholder",
    "The stakeholder relationship represents that an element (typically a business actor) is a stakeholder of another element, for example a project or decision."
  )
  val sourceTrait: Class[CanBeStakeholder] = classOf[CanBeStakeholder]
  val targetTrait: Class[CanHaveStakeholder] = classOf[CanHaveStakeholder]
  def withProperty(property: Property): Stakeholder =
    copy(properties = this.properties + (property.key -> property))
}

trait CanBeStakeholder extends CanBeRelationshipSource
trait CanHaveStakeholder extends CanBeRelationshipTarget

trait CanConfigureStakeholderSource[ModelComponentType <: CanBeStakeholder] {
  def relationshipAdder: CanAddRelationships
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def hasInterestIn(
      target: CanHaveStakeholder,
      title: String = "has interest in"
  ): RelationshipConfigurer =
    RelationshipConfigurer(
      isStakeholderFor(target, title),
      propertyAdder,
      relationshipAdder
    )

  def isStakeholderFor(
      target: CanHaveStakeholder,
      title: String = "is stakeholder for"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Stakeholder(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )
}

trait CanConfigureStakeHolderTarget[ModelComponentType <: CanHaveStakeholder] {
  def relationshipAdder: CanAddRelationships
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def isInterestOf(
      target: CanBeStakeholder,
      title: String = "has interest in"
  ): RelationshipConfigurer =
    RelationshipConfigurer(
      hasStakeholder(target, title),
      propertyAdder,
      relationshipAdder
    )

  def hasStakeholder(
      target: CanBeStakeholder,
      title: String = "is stakeholder for"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Stakeholder(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )
}
