package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class Enterprise(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title,
    description: Description = Description(None),
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDocumentation
    with CanCompose
    with CanBeComposedOf
    with CanBeServed
    with CanBeTriggered
    with CanTrigger {
  val layer: Layer = BusinessLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType("Enterprise")

  def withProperty(property: Property): Enterprise =
    copy(properties = this.properties + (property.key -> property))
}

trait HasEnterprises extends HasModelComponents {
  def enterprises: List[Enterprise] = components(classOf[Enterprise])
  def enterprise(key: Key): Option[Enterprise] =
    component(key, classOf[Enterprise])
}

case class EnterpriseConfigurer(
    modelComponent: Enterprise,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDocumentation[Enterprise]
    with CanConfigureTriggers[Enterprise] {
  def as(
      body: EnterpriseConfigurer => Any
  ): Enterprise = {
    body.apply(this)
    propertyAdder.townPlan
      .enterprise(modelComponent.key)
      .get
  }
}

trait CanAddEnterprises extends CanAddProperties with CanAddRelationships {
  def describes(enterprise: Enterprise): EnterpriseConfigurer =
    EnterpriseConfigurer(has(enterprise), this, this)
}
