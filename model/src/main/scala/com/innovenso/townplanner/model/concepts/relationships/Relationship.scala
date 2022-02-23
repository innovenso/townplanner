package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.{
  HasDescription,
  HasFatherTime
}
import com.innovenso.townplanner.model.language.{
  CanAddModelComponents,
  Concept,
  Element,
  HasModelComponents
}
import com.innovenso.townplanner.model.meta._

trait Relationship extends Concept with HasDescription with HasFatherTime {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Relationship"
  )
  val sortKey: SortKey = SortKey.next

  def key: Key

  def title: String
  def bidirectional: Boolean
  def source: Key
  def target: Key
  def relationshipType: RelationshipType
  def sourceTrait: Class[_ <: CanBeRelationshipSource]
  def targetTrait: Class[_ <: CanBeRelationshipTarget]
  def canHaveAsSource(element: Element): Boolean =
    sourceTrait.isInstance(element)
  def canHaveAsTarget(element: Element): Boolean =
    targetTrait.isInstance(element)
  def participants: Set[Key] = Set(source, target)
  def other(key: Key): Option[Key] = if (source == key) Some(target)
  else if (target == key) Some(source)
  else None
}

case class RelationshipType(name: String, description: String)

trait CanBeRelationshipSource extends Element
trait CanBeRelationshipTarget extends Element

trait HasRelationships extends HasModelComponents {
  def relationship(key: Key): Option[Relationship] =
    component(key, classOf[Relationship])

  def relationshipParticipants(key: Key): Set[Element] =
    relationship(key).map(r => relationshipParticipants(r)).getOrElse(Set())

  def relationshipParticipants(relationship: Relationship): Set[Element] =
    relationship.participants
      .map(component(_, classOf[Element]))
      .filter(_.nonEmpty)
      .map(_.get)

  def relationshipParticipantsOfType[ElementType <: Element](
      relationship: Relationship,
      elementClass: Class[ElementType]
  ): Set[ElementType] =
    relationshipParticipants(relationship)
      .filter(elementClass.isInstance(_))
      .map(elementClass.cast(_))

  def relationshipsWithSource[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationshipsWithSource(element).filter(
    otherElementTypeFilter(element, otherElementType)
  )

  def relationshipsWithSource(element: Element): List[Relationship] =
    relationships.filter(r => r.source == element.key)

  def relationships: List[Relationship] = components(classOf[Relationship])

  private def otherElementTypeFilter[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Relationship => Boolean = (relationship: Relationship) =>
    isOtherElementOfType(relationship, element.key, otherElementType)

  private def isOtherElementOfType[ElementType <: Element](
      relationship: Relationship,
      elementKey: Key,
      otherElementType: Class[ElementType]
  ): Boolean = component(
    relationship.other(elementKey).getOrElse(Key()),
    otherElementType
  ).isDefined

  def relationshipsWithType[RelationshipType <: Relationship](
      relationshipType: Class[RelationshipType]
  ): List[RelationshipType] = relationships
    .filter(relationshipType.isInstance(_))
    .map(relationshipType.cast(_))

  def relationshipsWithTarget[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationshipsWithTarget(element).filter(
    otherElementTypeFilter(element, otherElementType)
  )

  def relationshipsWithTarget(element: Element): List[Relationship] =
    relationships.filter(r => r.target == element.key)

  def directDependencies(element: Element): List[Element] =
    directDependenciesOfType(element, classOf[Element])

  def directDependenciesOfType[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[ElementType] = relationships(element)
    .flatMap(mapOtherElement(element, otherElementType))
    .distinct

  def directDependencies[RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType]
  ): List[Element] =
    directDependencies(element, relationshipType, classOf[Element])

  def directDependencies[
      ElementType <: Element,
      RelationshipType <: Relationship
  ](
      element: Element,
      relationshipType: Class[RelationshipType],
      otherElementType: Class[ElementType]
  ): List[ElementType] = relationships(element, relationshipType)
    .flatMap(mapOtherElement(element, otherElementType))
    .distinct

  def directIncomingDependencies(element: Element): List[Element] =
    directIncomingDependenciesOfType(element, classOf[Element])

  def directIncomingDependenciesOfType[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[ElementType] = relationshipsWithType(element, otherElementType)
    .filter(r => r.target == element.key)
    .flatMap(mapOtherElement(element, otherElementType))
    .distinct

  def relationshipsWithType[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationships(element).filter(
    otherElementTypeFilter(element, otherElementType)
  )

  def directIncomingDependencies[RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType]
  ): List[Element] =
    directIncomingDependencies(element, relationshipType, classOf[Element])

  def directIncomingDependencies[
      ElementType <: Element,
      RelationshipType <: Relationship
  ](
      element: Element,
      relationshipType: Class[RelationshipType],
      otherElementType: Class[ElementType]
  ): List[ElementType] =
    relationships(element, relationshipType, otherElementType)
      .filter(r => r.target == element.key)
      .flatMap(mapOtherElement(element, otherElementType))
      .distinct

  def directOutgoingDependencies(element: Element): List[Element] =
    directOutgoingDependenciesOfType(element, classOf[Element])

  def directOutgoingDependenciesOfType[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[ElementType] = relationshipsWithType(element, otherElementType)
    .filter(r => r.source == element.key)
    .flatMap(mapOtherElement(element, otherElementType))
    .distinct

  def directOutgoingDependencies[RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType]
  ): List[Element] =
    directOutgoingDependencies(element, relationshipType, classOf[Element])

  def directOutgoingDependencies[
      ElementType <: Element,
      RelationshipType <: Relationship
  ](
      element: Element,
      relationshipType: Class[RelationshipType],
      otherElementType: Class[ElementType]
  ): List[ElementType] =
    relationships(element, relationshipType, otherElementType)
      .filter(r => r.source == element.key)
      .flatMap(mapOtherElement(element, otherElementType))
      .distinct

  private def mapOtherElement[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Relationship => List[ElementType] = (relationship: Relationship) =>
    component(
      relationship.other(element.key).getOrElse(Key()),
      otherElementType
    ).toList

  def relationships[ElementType <: Element, RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType],
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationships(element, relationshipType).filter(
    otherElementTypeFilter(element, otherElementType)
  )

  def relationships[ElementType <: Element, RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType]
  ): List[Relationship] =
    relationships(element).filter(relationshipType.isInstance(_))

  def relationships(element: Element): List[Relationship] =
    relationships.filter(r =>
      r.source == element.key || r.target == element.key
    )
}

trait CanAddRelationships extends CanAddModelComponents {
  def hasRelationship(relationship: Relationship): Relationship = {
    val sourceOption = townPlan.component(relationship.source, classOf[Element])
    val targetOption = townPlan.component(relationship.target, classOf[Element])
    if (sourceOption.isEmpty || targetOption.isEmpty)
      throw new IllegalArgumentException(
        s"town plan does not contain source ${relationship.source.value} or target ${relationship.target.value}"
      )
    else if (!relationship.canHaveAsSource(sourceOption.get))
      throw new IllegalArgumentException(
        s"${relationship.relationshipType.name} can't have ${relationship.source.value} as source"
      )
    else if (!relationship.canHaveAsTarget(targetOption.get))
      throw new IllegalArgumentException(
        s"${relationship.relationshipType.name} can't have ${relationship.target.value} as target"
      )
    else
      has(relationship)
  }
}
