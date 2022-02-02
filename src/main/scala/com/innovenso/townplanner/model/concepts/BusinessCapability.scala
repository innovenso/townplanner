package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.{CanManipulateTownPlan, TownPlan}
import com.innovenso.townplanner.model.concepts.properties.{
  HasDocumentation,
  Property
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta.{
  ActiveStructure,
  Aspect,
  Behavior,
  BusinessLayer,
  Description,
  Key,
  Layer,
  ModelComponentType,
  PassiveStructure,
  SortKey,
  StrategyLayer,
  Title
}

import scala.util.{Success, Try}

case class BusinessCapability(
    key: Key,
    sortKey: SortKey,
    title: Title,
    description: Description,
    properties: Map[Key, Property]
) extends Element
    with HasDocumentation
    with CanBeRealized
    with CanBeServed
    with CanServe
    with CanBeFlowSource
    with CanBeFlowTarget
    with CanBeTriggered
    with CanTrigger {
  val layer: Layer = StrategyLayer
  val aspect: Aspect = Behavior
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Business Capability"
  )

  def withProperty(property: Property): BusinessCapability =
    copy(properties = this.properties + (property.key -> property))
}

trait HasBusinessCapabilities
    extends HasModelComponents
    with HasEnterprises
    with HasRelationships {
  def businessCapabilities: List[BusinessCapability] = components(
    classOf[BusinessCapability]
  )
  def businessCapability(key: Key): Option[BusinessCapability] =
    component(key, classOf[BusinessCapability])
  def level0businessCapabilities(
      enterprise: Enterprise
  ): List[BusinessCapability] = directIncomingDependencies(
    enterprise,
    Serves,
    classOf[BusinessCapability]
  ).toList.sortWith(_.sortKey < _.sortKey)
  def parentBusinessCapability(
      businessCapability: BusinessCapability
  ): Option[BusinessCapability] = directOutgoingDependencies(
    businessCapability,
    Serves,
    classOf[BusinessCapability]
  ).headOption
  def enterprise(businessCapability: BusinessCapability): Option[Enterprise] =
    directOutgoingDependencies(
      businessCapability,
      Serves,
      classOf[Enterprise]
    ).headOption.orElse(
      parentBusinessCapability(businessCapability).flatMap(bc => enterprise(bc))
    )
  def childBusinessCapabilities(
      businessCapability: BusinessCapability
  ): List[BusinessCapability] = directIncomingDependencies(
    businessCapability,
    Serves,
    classOf[BusinessCapability]
  ).toList.sortWith(_.sortKey < _.sortKey)
  private def traverseBusinessCapabilities(
      businessCapability: BusinessCapability
  ): LazyList[BusinessCapability] =
    businessCapability #:: (childBusinessCapabilities(
      businessCapability
    ) map traverseBusinessCapabilities).fold(LazyList.empty)(_ ++ _)

  def businessCapabilityMap(enterprise: Enterprise): List[BusinessCapability] =
    level0businessCapabilities(enterprise).flatMap(capability =>
      traverseBusinessCapabilities(capability)
    )
}

trait CanAddBusinessCapabilities
    extends CanManipulateTownPlan
    with CanAddRelationships {
  def withBusinessCapability(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None)
  ): Try[(TownPlan, BusinessCapability)] = withNewModelComponent(
    BusinessCapability(
      key = key,
      sortKey = sortKey,
      title = title,
      description = description,
      properties = Map.empty[Key, Property]
    )
  )

  def withChildBusinessCapability(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None),
      parent: BusinessCapability
  ): Try[(TownPlan, BusinessCapability)] =
    withBusinessCapability(key, sortKey, title, description).flatMap(tb =>
      withRelationship(
        title = Title(""),
        relationshipType = Serves,
        sourceKey = tb._2.key,
        targetKey = parent.key
      ).flatMap(tr => Success((tr._1, tb._2)))
    )

  def withEnterpriseBusinessCapability(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None),
      enterprise: Enterprise
  ): Try[(TownPlan, BusinessCapability)] =
    withBusinessCapability(key, sortKey, title, description).flatMap(tb =>
      withRelationship(
        title = Title(""),
        relationshipType = Serves,
        sourceKey = tb._2.key,
        targetKey = enterprise.key
      ).flatMap(tr => Success((tr._1, tb._2)))
    )

}
