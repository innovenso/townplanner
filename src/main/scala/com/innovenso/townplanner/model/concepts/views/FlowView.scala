package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.concepts.relationships.CanAddRelationships
import com.innovenso.townplanner.model.language.{
  Element,
  HasModelComponents,
  View
}
import com.innovenso.townplanner.model.meta._

case class FlowView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    withStepCounter: Boolean = true,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends View
    with HasDescription
    with HasInteractions
    with HasExternalIds
    with HasLinks {
  val modelComponentType: ModelComponentType = ModelComponentType("Flow View")
  val pointInTime: ADay = Today

  def withProperty(property: Property): FlowView =
    copy(properties = this.properties + (property.key -> property))

}

trait HasFlowViews extends HasModelComponents {
  def flowViews: List[FlowView] = components(classOf[FlowView])
  def flowView(key: Key): Option[FlowView] = component(key, classOf[FlowView])
  def elements(flowView: FlowView): List[Element] = flowView.interactions
    .flatMap(interaction => List(interaction.source, interaction.target))
    .distinct
    .map(component(_, classOf[Element]))
    .filter(_.nonEmpty)
    .map(_.get)
}

case class FlowViewConfigurer(
    modelComponent: FlowView,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[FlowView]
    with CanConfigureLinks[FlowView]
    with CanConfigureExternalIds[FlowView]
    with CanConfigureInteractions[FlowView] {
  def and(
      body: FlowViewConfigurer => Any
  ): FlowView = {
    body.apply(this)
    propertyAdder.townPlan
      .flowView(modelComponent.key)
      .get
  }
}

trait CanAddFlowViews extends CanAddProperties with CanAddRelationships {
  def needs(flowView: FlowView): FlowViewConfigurer =
    FlowViewConfigurer(has(flowView), this, this)
}
