package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAddRelationships,
  Composition,
  HasRelationships,
  Implementation
}
import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.language._
import com.innovenso.townplanner.model.meta._

case class FlowView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    withStepCounter: Boolean = true,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends TimelessView
    with HasDescription
    with HasInteractions
    with HasExternalIds
    with HasLinks {
  val modelComponentType: ModelComponentType = ModelComponentType("Flow View")
  val layer: Layer = ApplicationLayer

  def withProperty(property: Property): FlowView =
    copy(properties = this.properties + (property.key -> property))

}

trait HasFlowViews extends HasViews with HasTechnologies {
  def flowViews: List[CompiledFlowView] =
    components(classOf[FlowView]).map(FlowViewCompiler(_, this).compile)
  def flowView(key: Key): Option[CompiledFlowView] =
    component(key, classOf[FlowView]).map(FlowViewCompiler(_, this).compile)
}

case class CompiledFlowView(
    view: FlowView,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[FlowView]
    with HasItSystems
    with HasItContainers
    with HasItPlatforms
    with HasRelationships
    with HasBusinessActors
    with HasTechnologies {
  def otherSystems: List[ItSystem] = systems.filterNot(systemContexts.toSet)

  def systemContexts: List[ItSystem] =
    containers.map(system).filter(_.nonEmpty).map(_.get)

  def systemsInSteps: List[ItSystem] = interactions
    .flatMap(i =>
      List(stepSource(i), stepTarget(i))
        .filter(_.isInstanceOf[ItSystem])
        .map(_.asInstanceOf[ItSystem])
    )
    .distinct

  def withStepCounter: Boolean = view.withStepCounter

  def steps: List[(Interaction, Int)] =
    interactions.zip(LazyList from 1)

  def interactions: List[Interaction] = view.interactions

  def stepSource(step: Interaction): Element =
    component(step.source, classOf[Element]).get

  def stepTarget(step: Interaction): Element =
    component(step.target, classOf[Element]).get
}

case class FlowViewCompiler(
    view: FlowView,
    source: HasFlowViews
) extends ViewCompiler[
      FlowView,
      CompiledFlowView,
      HasFlowViews
    ] {
  def compile: CompiledFlowView =
    CompiledFlowView(
      view,
      viewTitle,
      "Flows",
      viewComponents(allElements ++ compositions ++ implementingTechnologies)
    )

  private def elements: List[Element] =
    view.interactions
      .flatMap(interaction => List(interaction.source, interaction.target))
      .distinct
      .map(source.component(_, classOf[Element]))
      .filter(_.nonEmpty)
      .map(_.get)

  private def systemContexts: List[ItSystem] =
    elements
      .filter(_.isInstanceOf[ItContainer])
      .flatMap(container =>
        source
          .relationships(container, classOf[Composition], classOf[ItSystem])
          .flatMap(r =>
            source.relationshipParticipantsOfType(r, classOf[ItSystem])
          )
      )

  private def allElements: List[Element] =
    elements ++ systemContexts ++ technologies ++ actors

  private def containers: Set[ItContainer] = elements
    .filter(_.isInstanceOf[ItContainer])
    .map(_.asInstanceOf[ItContainer])
    .toSet

  private def actors: Set[BusinessActor] = elements
    .filter(_.isInstanceOf[BusinessActor])
    .map(_.asInstanceOf[BusinessActor])
    .toSet

  private def technologies: Set[Technology] =
    containers.flatMap(source.technologies(_))

  private def compositions: List[Composition] = allElements.flatMap(element =>
    source
      .relationships(element, classOf[Composition])
      .filter(r =>
        r.other(element.key).exists(o => elements.exists(_.key == o))
      )
      .map(_.asInstanceOf[Composition])
  )

  def implementingTechnologies: Set[Implementation] = technologies
    .flatMap(it =>
      source.relationships(it, classOf[Implementation], classOf[ItContainer])
    )
    .filter(r =>
      source
        .relationshipParticipantsOfType(r, classOf[ItContainer])
        .forall(containers.contains(_))
    )
    .map(_.asInstanceOf[Implementation])

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
    propertyAdder.townPlan.component(modelComponent.key, classOf[FlowView]).get
  }
}

trait CanAddFlowViews extends CanAddProperties with CanAddRelationships {
  def needs(flowView: FlowView): FlowViewConfigurer =
    FlowViewConfigurer(has(flowView), this, this)
}
