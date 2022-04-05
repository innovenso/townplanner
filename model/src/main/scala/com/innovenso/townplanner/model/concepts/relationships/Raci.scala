package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.{CanAddProperties, Property}
import com.innovenso.townplanner.model.meta.Key

trait RACI extends Relationship

case class Responsible(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "is responsible for",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends RACI {
  val relationshipType: RelationshipType = RelationshipType(
    "is responsible for",
    "The responsible relationship represents that a element, typically an individual or team, is responsible for another element."
  )
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
  def withProperty(property: Property): Responsible =
    copy(properties = this.properties + (property.key -> property))
}

trait CanBeRaci extends CanBeRelationshipSource
trait CanHaveRaci extends CanBeRelationshipTarget

case class Accountable(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "is accountable for",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends RACI {
  val relationshipType: RelationshipType = RelationshipType(
    "is accountable for",
    "The accountable relationship represents that a element, typically an individual or team, is accountable for another element."
  )
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
  def withProperty(property: Property): Accountable =
    copy(properties = this.properties + (property.key -> property))
}

case class Consulted(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "is consulted about",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends RACI {
  val relationshipType: RelationshipType = RelationshipType(
    "is consulted about",
    "The consulted relationship represents that a element, typically an individual or team, has been consulted about another element."
  )
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
  def withProperty(property: Property): Consulted =
    copy(properties = this.properties + (property.key -> property))
}

case class Informed(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "is informed about",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends RACI {
  val relationshipType: RelationshipType = RelationshipType(
    "is informed about",
    "The informed relationship represents that a element, typically an individual or team, has been informed about another element."
  )
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
  def withProperty(property: Property): Informed =
    copy(properties = this.properties + (property.key -> property))
}

trait CanConfigureRaciSource[ModelComponentType <: CanBeRaci] {
  def relationshipAdder: CanAddRelationships
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def hasResponsibilityFor(target: CanHaveRaci, title: String = "is responsible for"): RelationshipConfigurer =
    RelationshipConfigurer(isResponsibleFor(target, title), propertyAdder, relationshipAdder)

  def isResponsibleFor(
      target: CanHaveRaci,
      title: String = "is responsible for"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Responsible(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )

  def hasAccountabilityFor(target: CanHaveRaci, title: String = "is accountable for"): RelationshipConfigurer =
    RelationshipConfigurer(isAccountableFor(target, title), propertyAdder, relationshipAdder)

  def isAccountableFor(
      target: CanHaveRaci,
      title: String = "is accountable for"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Accountable(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )

  def hasBeenConsultedAbout(target: CanHaveRaci, title: String = "is consulted about"): RelationshipConfigurer =
    RelationshipConfigurer(isConsultedAbout(target, title), propertyAdder, relationshipAdder)

  def isConsultedAbout(
      target: CanHaveRaci,
      title: String = "is consulted about"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Consulted(source = modelComponent.key, target = target.key, title = title)
    )

  def hasBeenInformedAbout(target: CanHaveRaci, title: String = "is informed about"): RelationshipConfigurer =
    RelationshipConfigurer(isInformedAbout(target, title), propertyAdder, relationshipAdder)

  def isInformedAbout(
      target: CanHaveRaci,
      title: String = "is informed about"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Informed(source = modelComponent.key, target = target.key, title = title)
    )

}

trait CanConfigureRaciTarget[ModelComponentType <: CanHaveRaci] {
  def relationshipAdder: CanAddRelationships
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def hasResponsible(target: CanBeRaci, title: String = "is responsible for"): RelationshipConfigurer =
    RelationshipConfigurer(isResponsibilityOf(target, title), propertyAdder, relationshipAdder)

  def isResponsibilityOf(
      target: CanBeRaci,
      title: String = "is responsible for"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Responsible(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )

  def hasAccountable(target: CanBeRaci, title: String = "is accountable for"): RelationshipConfigurer =
    RelationshipConfigurer(isAccountabilityOf(target, title), propertyAdder, relationshipAdder)

  def isAccountabilityOf(
      target: CanBeRaci,
      title: String = "is accountable for"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Accountable(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )

  def consulted(target: CanBeRaci, title: String = "is consulted about"): RelationshipConfigurer =
    RelationshipConfigurer(hasConsulted(target, title), propertyAdder, relationshipAdder)

  def hasConsulted(
      target: CanBeRaci,
      title: String = "is consulted about"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Consulted(source = target.key, target = modelComponent.key, title = title)
    )

  def informed(target: CanBeRaci, title: String = "is informed about"): RelationshipConfigurer =
    RelationshipConfigurer(hasInformed(target, title), propertyAdder, relationshipAdder)

  def hasInformed(
      target: CanBeRaci,
      title: String = "is informed about"
  ): Relationship =
    relationshipAdder.hasRelationship(
      Informed(source = target.key, target = modelComponent.key, title = title)
    )

}
