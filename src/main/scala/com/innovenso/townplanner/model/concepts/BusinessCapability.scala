package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  CanConfigureArchitectureVerdict,
  CanConfigureDescription,
  HasArchitectureVerdict,
  HasDescription,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class BusinessCapability(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDescription
    with HasArchitectureVerdict
    with CanBeRealized
    with CanBeServed
    with CanServe
    with CanBeFlowSource
    with CanBeFlowTarget
    with CanBeTriggered
    with CanBeImpacted
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
    classOf[Serving],
    classOf[BusinessCapability]
  ).sortWith(_.sortKey < _.sortKey)
  def parentBusinessCapability(
      businessCapability: BusinessCapability
  ): Option[BusinessCapability] = directOutgoingDependencies(
    businessCapability,
    classOf[Serving],
    classOf[BusinessCapability]
  ).headOption
  def enterprise(businessCapability: BusinessCapability): Option[Enterprise] =
    directOutgoingDependencies(
      businessCapability,
      classOf[Serving],
      classOf[Enterprise]
    ).headOption.orElse(
      parentBusinessCapability(businessCapability).flatMap(bc => enterprise(bc))
    )
  def childBusinessCapabilities(
      businessCapability: BusinessCapability
  ): List[BusinessCapability] = directIncomingDependencies(
    businessCapability,
    classOf[Serving],
    classOf[BusinessCapability]
  ).sortWith(_.sortKey < _.sortKey)

  def businessCapabilityMap(enterprise: Enterprise): List[BusinessCapability] =
    level0businessCapabilities(enterprise).flatMap(capability =>
      traverseBusinessCapabilities(capability)
    )

  private def traverseBusinessCapabilities(
      businessCapability: BusinessCapability
  ): LazyList[BusinessCapability] =
    businessCapability #:: (childBusinessCapabilities(
      businessCapability
    ) map traverseBusinessCapabilities).fold(LazyList.empty)(_ ++ _)
}

case class BusinessCapabilityMapConfigurerConfigurer(
    modelComponent: BusinessCapability,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[BusinessCapability]
    with CanConfigureArchitectureVerdict[BusinessCapability]
    with CanConfigureServingSource[BusinessCapability]
    with CanConfigureServingTarget[BusinessCapability]
    with CanConfigureRealizationTarget[BusinessCapability]
    with CanConfigureFlowSource[BusinessCapability]
    with CanConfigureFlowTarget[BusinessCapability]
    with CanConfigureTriggerSource[BusinessCapability]
    with CanConfigureTriggerTarget[BusinessCapability] {
  def as(
      body: BusinessCapabilityMapConfigurerConfigurer => Any
  ): BusinessCapability = {
    body.apply(this)
    propertyAdder.townPlan
      .businessCapability(modelComponent.key)
      .get
  }
}

trait CanAddBusinessCapabilities
    extends CanAddProperties
    with CanAddRelationships {
  def describes(
      businessCapability: BusinessCapability
  ): BusinessCapabilityMapConfigurerConfigurer =
    BusinessCapabilityMapConfigurerConfigurer(
      has(businessCapability),
      this,
      this
    )
}
