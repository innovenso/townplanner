package com.innovenso.townplanner.model.concepts.relationships

import com.innovenso.townplanner.model.concepts.properties.HasDescription
import com.innovenso.townplanner.model.language.{
  CanAddModelComponents,
  Concept,
  Element,
  HasModelComponents
}
import com.innovenso.townplanner.model.meta._

trait Relationship extends Concept with HasDescription {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Relationship"
  )
  val sortKey: SortKey = SortKey(None)

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

  def relationshipsWithSource[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationshipsWithSource(element).filter(
    otherElementTypeFilter(element, otherElementType)
  )

  def relationshipsWithSource(element: Element): List[Relationship] =
    relationships.filter(r => r.source == element.key)

  def relationshipsWithTarget[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationshipsWithTarget(element).filter(
    otherElementTypeFilter(element, otherElementType)
  )

  def relationshipsWithTarget(element: Element): List[Relationship] =
    relationships.filter(r => r.target == element.key)

  def directDependencies(element: Element): Set[Element] =
    directDependenciesOfType(element, classOf[Element])

  def directDependenciesOfType[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Set[ElementType] = relationships(element)
    .flatMap(mapOtherElement(element, otherElementType))
    .toSet

  def directDependencies[RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType]
  ): Set[Element] =
    directDependencies(element, relationshipType, classOf[Element])

  def directDependencies[
      ElementType <: Element,
      RelationshipType <: Relationship
  ](
      element: Element,
      relationshipType: Class[RelationshipType],
      otherElementType: Class[ElementType]
  ): Set[ElementType] = relationships(element, relationshipType)
    .flatMap(mapOtherElement(element, otherElementType))
    .toSet

  def directIncomingDependencies(element: Element): Set[Element] =
    directIncomingDependenciesOfType(element, classOf[Element])

  def directIncomingDependenciesOfType[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Set[ElementType] = relationshipsWithType(element, otherElementType)
    .filter(r => r.target == element.key)
    .flatMap(mapOtherElement(element, otherElementType))
    .toSet

  def directIncomingDependencies[RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType]
  ): Set[Element] =
    directIncomingDependencies(element, relationshipType, classOf[Element])

  def directIncomingDependencies[
      ElementType <: Element,
      RelationshipType <: Relationship
  ](
      element: Element,
      relationshipType: Class[RelationshipType],
      otherElementType: Class[ElementType]
  ): Set[ElementType] =
    relationships(element, relationshipType, otherElementType)
      .filter(r => r.target == element.key)
      .flatMap(mapOtherElement(element, otherElementType))
      .toSet

  private def mapOtherElement[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Relationship => Set[ElementType] = (relationship: Relationship) =>
    component(
      relationship.other(element.key).getOrElse(Key()),
      otherElementType
    ).toSet

  def relationships[ElementType <: Element, RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType],
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationships(element, relationshipType).filter(
    otherElementTypeFilter(element, otherElementType)
  )

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

  def relationships[ElementType <: Element, RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType]
  ): List[Relationship] =
    relationships(element).filter(relationshipType.isInstance(_))

  def relationships(element: Element): List[Relationship] =
    relationships.filter(r =>
      r.source == element.key || r.target == element.key
    )

  def relationships: List[Relationship] = components(classOf[Relationship])

  def directOutgoingDependencies(element: Element): Set[Element] =
    directOutgoingDependenciesOfType(element, classOf[Element])

  def directOutgoingDependenciesOfType[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Set[ElementType] = relationshipsWithType(element, otherElementType)
    .filter(r => r.source == element.key)
    .flatMap(mapOtherElement(element, otherElementType))
    .toSet

  def relationshipsWithType[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationships(element).filter(
    otherElementTypeFilter(element, otherElementType)
  )

  def directOutgoingDependencies[RelationshipType <: Relationship](
      element: Element,
      relationshipType: Class[RelationshipType]
  ): Set[Element] =
    directOutgoingDependencies(element, relationshipType, classOf[Element])

  def directOutgoingDependencies[
      ElementType <: Element,
      RelationshipType <: Relationship
  ](
      element: Element,
      relationshipType: Class[RelationshipType],
      otherElementType: Class[ElementType]
  ): Set[ElementType] =
    relationships(element, relationshipType, otherElementType)
      .filter(r => r.source == element.key)
      .flatMap(mapOtherElement(element, otherElementType))
      .toSet
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
