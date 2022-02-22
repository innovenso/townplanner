package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.Property
import com.innovenso.townplanner.model.meta.Key

case class Responsible(
    key: Key = Key(),
    source: Key,
    target: Key,
    title: String = "is responsible for",
    bidirectional: Boolean = false,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Relationship {
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
) extends Relationship {
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
) extends Relationship {
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
) extends Relationship {
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
  def modelComponent: ModelComponentType

  def isResponsibleFor(target: CanHaveRaci): Relationship =
    isResponsibleFor(target, "is responsible for")
  def isResponsibleFor(
      target: CanHaveRaci,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Responsible(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )

  def isAccountableFor(target: CanHaveRaci): Relationship =
    isAccountableFor(target, "is accountable for")
  def isAccountableFor(
      target: CanHaveRaci,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Accountable(
        source = modelComponent.key,
        target = target.key,
        title = title
      )
    )

  def isConsultedAbout(target: CanHaveRaci): Relationship =
    isConsultedAbout(target, "is consulted about")
  def isConsultedAbout(
      target: CanHaveRaci,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Consulted(source = modelComponent.key, target = target.key, title = title)
    )

  def isInformedAbout(target: CanHaveRaci): Relationship =
    isConsultedAbout(target, "is informed about")
  def isInformedAbout(
      target: CanHaveRaci,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Informed(source = modelComponent.key, target = target.key, title = title)
    )

}

trait CanConfigureRaciTarget[ModelComponentType <: CanHaveRaci] {
  def relationshipAdder: CanAddRelationships
  def modelComponent: ModelComponentType

  def isResponsibilityOf(target: CanBeRaci): Relationship =
    isResponsibilityOf(target, "is responsible for")
  def isResponsibilityOf(
      target: CanBeRaci,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Responsible(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )

  def isAccountabilityOf(target: CanBeRaci): Relationship =
    isAccountabilityOf(target, "is accountable for")
  def isAccountabilityOf(
      target: CanBeRaci,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Accountable(
        source = target.key,
        target = modelComponent.key,
        title = title
      )
    )

  def hasConsulted(target: CanBeRaci): Relationship =
    hasConsulted(target, "is consulted about")
  def hasConsulted(
      target: CanBeRaci,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Consulted(source = target.key, target = modelComponent.key, title = title)
    )

  def hasInformed(target: CanBeRaci): Relationship =
    hasInformed(target, "is informed about")
  def hasInformed(
      target: CanBeRaci,
      title: String
  ): Relationship =
    relationshipAdder.hasRelationship(
      Informed(source = target.key, target = modelComponent.key, title = title)
    )

}
