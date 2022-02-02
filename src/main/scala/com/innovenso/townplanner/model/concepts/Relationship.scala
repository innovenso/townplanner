package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.language.{
  Concept,
  Element,
  HasModelComponents
}
import com.innovenso.townplanner.model.meta._
import com.innovenso.townplanner.model.{CanManipulateTownPlan, TownPlan}

import java.lang.annotation.ElementType
import scala.util.{Failure, Try}

case class Relationship(
    key: Key,
    sortKey: SortKey,
    title: Title,
    description: Description,
    bidirectional: Boolean = false,
    source: Key,
    target: Key,
    relationshipType: RelationshipType
) extends Concept {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Relationship"
  )
  def other(key: Key): Option[Key] = if (source == key) Some(target)
  else if (target == key) Some(source)
  else None
}

trait HasRelationships extends HasModelComponents {
  def relationships: List[Relationship] = components(classOf[Relationship])
  def relationship(key: Key): Option[Relationship] =
    component(key, classOf[Relationship])
  def relationships(relationshipType: RelationshipType): List[Relationship] =
    relationships.filter(r => r.relationshipType == relationshipType)
  def relationships(element: Element): List[Relationship] =
    relationships.filter(r =>
      r.source == element.key || r.target == element.key
    )
  def relationships[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationships(element).filter(
    otherElementTypeFilter(element, otherElementType)
  )

  def relationships(
      element: Element,
      relationshipType: RelationshipType
  ): List[Relationship] =
    relationships(element).filter(r => r.relationshipType == relationshipType)
  def relationships[ElementType <: Element](
      element: Element,
      relationshipType: RelationshipType,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationships(element, relationshipType).filter(
    otherElementTypeFilter(element, otherElementType)
  )
  def relationshipsWithSource(element: Element): List[Relationship] =
    relationships.filter(r => r.source == element.key)
  def relationshipsWithSource[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationshipsWithSource(element).filter(
    otherElementTypeFilter(element, otherElementType)
  )
  def relationshipsWithTarget(element: Element): List[Relationship] =
    relationships.filter(r => r.target == element.key)
  def relationshipsWithTarget[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): List[Relationship] = relationshipsWithTarget(element).filter(
    otherElementTypeFilter(element, otherElementType)
  )
  def relationshipsWithSource(
      element: Element,
      relationshipType: RelationshipType
  ): List[Relationship] = relationshipsWithSource(element).filter(r =>
    r.relationshipType == relationshipType
  )
  def relationshipsWithSource[ElementType <: Element](
      element: Element,
      relationshipType: RelationshipType,
      otherElementType: Class[ElementType]
  ): List[Relationship] =
    relationshipsWithSource(element, relationshipType).filter(
      otherElementTypeFilter(element, otherElementType)
    )
  def relationshipsWithTarget(
      element: Element,
      relationshipType: RelationshipType
  ): List[Relationship] = relationshipsWithTarget(element).filter(r =>
    r.relationshipType == relationshipType
  )
  def relationshipsWithTarget[ElementType <: Element](
      element: Element,
      relationshipType: RelationshipType,
      otherElementType: Class[ElementType]
  ): List[Relationship] =
    relationshipsWithTarget(element, relationshipType).filter(
      otherElementTypeFilter(element, otherElementType)
    )
  def directDependencies[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Set[ElementType] = relationships(element)
    .flatMap(mapOtherElement(element, otherElementType))
    .toSet
  def directDependencies(element: Element): Set[Element] =
    directDependencies(element, classOf[Element])
  def directDependencies[ElementType <: Element](
      element: Element,
      relationshipType: RelationshipType,
      otherElementType: Class[ElementType]
  ): Set[ElementType] = relationships(element, relationshipType)
    .flatMap(mapOtherElement(element, otherElementType))
    .toSet
  def directDependencies(
      element: Element,
      relationshipType: RelationshipType
  ): Set[Element] =
    directDependencies(element, relationshipType, classOf[Element])

  def directIncomingDependencies[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Set[ElementType] = relationships(element, otherElementType)
    .filter(r => r.target == element.key)
    .flatMap(mapOtherElement(element, otherElementType))
    .toSet
  def directIncomingDependencies(element: Element): Set[Element] =
    directIncomingDependencies(element, classOf[Element])

  def directIncomingDependencies[ElementType <: Element](
      element: Element,
      relationshipType: RelationshipType,
      otherElementType: Class[ElementType]
  ): Set[ElementType] =
    relationships(element, relationshipType, otherElementType)
      .filter(r => r.target == element.key)
      .flatMap(mapOtherElement(element, otherElementType))
      .toSet
  def directIncomingDependencies(
      element: Element,
      relationshipType: RelationshipType
  ): Set[Element] =
    directIncomingDependencies(element, relationshipType, classOf[Element])

  def directOutgoingDependencies[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Set[ElementType] = relationships(element, otherElementType)
    .filter(r => r.source == element.key)
    .flatMap(mapOtherElement(element, otherElementType))
    .toSet
  def directOutgoingDependencies(element: Element): Set[Element] =
    directOutgoingDependencies(element, classOf[Element])

  def directOutgoingDependencies[ElementType <: Element](
      element: Element,
      relationshipType: RelationshipType,
      otherElementType: Class[ElementType]
  ): Set[ElementType] =
    relationships(element, relationshipType, otherElementType)
      .filter(r => r.source == element.key)
      .flatMap(mapOtherElement(element, otherElementType))
      .toSet
  def directOutgoingDependencies(
      element: Element,
      relationshipType: RelationshipType
  ): Set[Element] =
    directOutgoingDependencies(element, relationshipType, classOf[Element])

  private def isOtherElementOfType[ElementType <: Element](
      relationship: Relationship,
      elementKey: Key,
      otherElementType: Class[ElementType]
  ): Boolean = component(
    relationship.other(elementKey).getOrElse(Key()),
    otherElementType
  ).isDefined
  private def otherElementTypeFilter[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Relationship => Boolean = (relationship: Relationship) =>
    isOtherElementOfType(relationship, element.key, otherElementType)
  private def mapOtherElement[ElementType <: Element](
      element: Element,
      otherElementType: Class[ElementType]
  ): Relationship => Set[ElementType] = (relationship: Relationship) =>
    component(
      relationship.other(element.key).getOrElse(Key()),
      otherElementType
    ).toSet
}

trait CanAddRelationships extends CanManipulateTownPlan {
  def withRelationship(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None),
      bidirectional: Boolean = false,
      sourceKey: Key,
      targetKey: Key,
      relationshipType: RelationshipType
  ): Try[(TownPlan, Relationship)] = {
    val sourceOption = townPlan.component(sourceKey, classOf[Element])
    val targetOption = townPlan.component(targetKey, classOf[Element])
    if (sourceOption.isEmpty || targetOption.isEmpty)
      Failure(
        new IllegalArgumentException(
          s"town plan does not contain source ${sourceKey.value} or target ${targetKey.value}"
        )
      )
    else if (!relationshipType.canHaveAsSource(sourceOption.get))
      Failure(
        new IllegalArgumentException(
          s"${relationshipType.name} can't have ${sourceKey.value} as source"
        )
      )
    else if (!relationshipType.canHaveAsTarget(targetOption.get))
      Failure(
        new IllegalArgumentException(
          s"${relationshipType.name} can't have ${targetKey.value} as target"
        )
      )
    else
      withNewModelComponent(
        Relationship(
          key = key,
          sortKey = sortKey,
          title = title,
          description = description,
          bidirectional = bidirectional,
          source = sourceKey,
          target = targetKey,
          relationshipType = relationshipType
        )
      )
  }
}

trait CanBeRelationshipSource
trait CanBeRelationshipTarget

sealed trait RelationshipType {
  def name: String
  def description: String
  def sourceTrait: Class[_ <: CanBeRelationshipSource]
  def targetTrait: Class[_ <: CanBeRelationshipTarget]
  def canHaveAsSource(element: Element): Boolean =
    sourceTrait.isInstance(element)
  def canHaveAsTarget(element: Element): Boolean =
    targetTrait.isInstance(element)
}

case object Flows extends RelationshipType {
  val name: String = "flows"
  val description: String =
    "The flow relationship represents transfer from one element to another."
  val sourceTrait: Class[CanBeFlowSource] = classOf[CanBeFlowSource]
  val targetTrait: Class[CanBeFlowTarget] = classOf[CanBeFlowTarget]
}

trait CanBeFlowSource extends CanBeRelationshipSource
trait CanBeFlowTarget extends CanBeRelationshipTarget

case object Triggers extends RelationshipType {
  val name: String = "triggers"
  val description: String =
    "The triggering relationship represents a temporal or causal relationship between elements."
  val sourceTrait: Class[CanTrigger] = classOf[CanTrigger]
  val targetTrait: Class[CanBeTriggered] = classOf[CanBeTriggered]
}

trait CanTrigger extends CanBeRelationshipSource
trait CanBeTriggered extends CanBeRelationshipTarget

case object Realizes extends RelationshipType {
  val name: String = "realizes"
  val description: String =
    "The realization relationship represents that an entity plays a critical role in the creation, achievement, sustenance, or operation of a more abstract entity."
  val sourceTrait: Class[CanRealize] = classOf[CanRealize]
  val targetTrait: Class[CanBeRealized] = classOf[CanBeRealized]
}

trait CanRealize extends CanBeRelationshipSource
trait CanBeRealized extends CanBeRelationshipTarget

case object IsComposedOf extends RelationshipType {
  val name: String = "is composed of"
  val description: String =
    "The composition relationship represents that an element consists of one or more other concepts."

  val sourceTrait: Class[CanBeComposedOf] = classOf[CanBeComposedOf]
  val targetTrait: Class[CanCompose] = classOf[CanCompose]

}

trait CanBeComposedOf extends CanBeRelationshipSource
trait CanCompose extends CanBeRelationshipTarget

case object IsStakeholderFor extends RelationshipType {
  val name: String = "is stakeholder for"
  val description: String =
    "The stakeholder relationship represents that an element (typically a business actor) is a stakeholder of another element, for example a project or decision."

  val sourceTrait: Class[CanBeStakeholder] = classOf[CanBeStakeholder]
  val targetTrait: Class[CanHaveStakeholder] = classOf[CanHaveStakeholder]
}

trait CanBeStakeholder extends CanBeRelationshipSource
trait CanHaveStakeholder extends CanBeRelationshipTarget

case object Delivers extends RelationshipType {
  val name: String = "delivers"
  val description: String =
    "The delivery relationship represents that an element such as an individual or a team is responsible for the delivery of another element."
  val sourceTrait: Class[CanDeliver] = classOf[CanDeliver]
  val targetTrait: Class[CanBeDelivered] = classOf[CanBeDelivered]
}

trait CanDeliver extends CanBeRelationshipSource
trait CanBeDelivered extends CanBeRelationshipTarget

case object ImpactsAndCreates extends RelationshipType {
  val name: String = "creates"
  val description: String =
    "The creates relationship represents that a element, such as a project or decision, adds another element to the architecture."
  val sourceTrait: Class[CanImpact] = classOf[CanImpact]
  val targetTrait: Class[CanBeImpacted] = classOf[CanBeImpacted]
}

trait CanImpact extends CanBeRelationshipSource
trait CanBeImpacted extends CanBeRelationshipTarget

case object ImpactsAndRemoves extends RelationshipType {
  val name: String = "removes"
  val description: String =
    "The removes relationship represents that a element, such as a project or decision, removes another element from the architecture."
  val sourceTrait: Class[CanImpact] = classOf[CanImpact]
  val targetTrait: Class[CanBeImpacted] = classOf[CanBeImpacted]
}

case object ImpactsAndChanges extends RelationshipType {
  val name: String = "changes"
  val description: String =
    "The changes relationship represents that a element, such as a project or decision, changes another element in the architecture."
  val sourceTrait: Class[CanImpact] = classOf[CanImpact]
  val targetTrait: Class[CanBeImpacted] = classOf[CanBeImpacted]
}

case object ImpactsAndKeeps extends RelationshipType {
  val name: String = "keeps"
  val description: String =
    "The keeps relationship represents that a element, such as a project or decision, keeps another element in the architecture as it is."
  val sourceTrait: Class[CanImpact] = classOf[CanImpact]
  val targetTrait: Class[CanBeImpacted] = classOf[CanBeImpacted]
}

case object IsResponsibleFor extends RelationshipType {
  val name: String = "is responsible for"
  val description: String =
    "The responsible relationship represents that a element, typically an individual or team, is responsible for another element."
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
}

trait CanBeRaci extends CanBeRelationshipSource
trait CanHaveRaci extends CanBeRelationshipTarget

case object IsAccountableFor extends RelationshipType {
  val name: String = "is accountable for"
  val description: String =
    "The accountable relationship represents that a element, typically an individual or team, is accountable for another element."
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
}

case object HasBeenConsultedAbout extends RelationshipType {
  val name: String = "has been consulted about"
  val description: String =
    "The responsible relationship represents that a element, typically an individual or team, has been consulted about another element."
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
}

case object IsToBeConsultedAbout extends RelationshipType {
  val name: String = "is to be consulted about"
  val description: String =
    "The consulted relationship represents that a element, typically an individual or team, is to consulted about another element."
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
}

case object HasBeenInformedAbout extends RelationshipType {
  val name: String = "has been informed about"
  val description: String =
    "The informed relationship represents that a element, typically an individual or team, has been informed about another element."
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
}

case object IsToBeInformedAbout extends RelationshipType {
  val name: String = "is to be informed about"
  val description: String =
    "The informed relationship represents that a element, typically an individual or team, is to informed about another element."
  val sourceTrait: Class[CanBeRaci] = classOf[CanBeRaci]
  val targetTrait: Class[CanHaveRaci] = classOf[CanHaveRaci]
}

case object Influences extends RelationshipType {
  val name: String = "influences"
  val description: String =
    "The influences relationship represents that a element influences another element. For example, a business stakeholder can influence a decision."
  val sourceTrait: Class[CanInfluence] = classOf[CanInfluence]
  val targetTrait: Class[CanBeInfluenced] = classOf[CanBeInfluenced]
}

trait CanInfluence extends CanBeRelationshipSource
trait CanBeInfluenced extends CanBeRelationshipTarget

case object IsAssociatedWith extends RelationshipType {
  val name: String = "is associated with"
  val description: String =
    "An association relationship represents an unspecified relationship, or one that is not represented by another relationship."
  val sourceTrait: Class[CanBeAssociated] = classOf[CanBeAssociated]
  val targetTrait: Class[CanBeAssociated] = classOf[CanBeAssociated]
}

trait CanBeAssociated
    extends CanBeRelationshipSource
    with CanBeRelationshipTarget

case object IsImplementedBy extends RelationshipType {
  val name: String = "is implemented by"
  val description: String =
    "An implementation relationship represents that one element is partly, or completely implemented by another element. A system can be implemented by a technology or by an infrastructure component for example"
  val sourceTrait: Class[CanBeImplemented] = classOf[CanBeImplemented]
  val targetTrait: Class[CanImplement] = classOf[CanImplement]
}

trait CanBeImplemented extends CanBeRelationshipSource
trait CanImplement extends CanBeRelationshipTarget

case object Serves extends RelationshipType {
  val name: String = "serves"
  val description: String =
    "The serving relationship represents that an element provides its functionality to another element."
  val sourceTrait: Class[CanServe] = classOf[CanServe]
  val targetTrait: Class[CanBeServed] = classOf[CanBeServed]
}

trait CanServe extends CanBeRelationshipSource
trait CanBeServed extends CanBeRelationshipTarget
