package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{BusinessCapability, Enterprise}
import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  CanConfigureDescription,
  CanConfigureExternalIds,
  CanConfigureInteractions,
  CanConfigureLinks,
  CanConfigureSWOT,
  HasDescription,
  HasExternalIds,
  HasInteractions,
  HasLinks,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAddRelationships,
  CanConfigureCompositionSource,
  CanConfigureCompositionTarget,
  CanConfigureServingTarget
}
import com.innovenso.townplanner.model.language.{Concept, HasModelComponents}
import com.innovenso.townplanner.model.meta.{Key, ModelComponentType, SortKey}

case class FlowView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Concept
    with HasDescription
    with HasInteractions
    with HasExternalIds
    with HasLinks {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Flow View"
  )

  def withProperty(property: Property): FlowView =
    copy(properties = this.properties + (property.key -> property))

}

trait HasFlowViews extends HasModelComponents {
  def flowViews: List[FlowView] = components(classOf[FlowView])
  def flowView(key: Key): Option[FlowView] = component(key, classOf[FlowView])
}

case class FlowViewConfigurer(
    modelComponent: FlowView,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[FlowView]
    with CanConfigureLinks[FlowView]
    with CanConfigureExternalIds[FlowView]
    with CanConfigureInteractions[FlowView] {
  def as(
      body: FlowViewConfigurer => Any
  ): FlowView = {
    body.apply(this)
    propertyAdder.townPlan
      .flowView(modelComponent.key)
      .get
  }
}

trait CanAddFlowViews extends CanAddProperties with CanAddRelationships {
  def describes(flowView: FlowView): FlowViewConfigurer =
    FlowViewConfigurer(has(flowView), this, this)
}
