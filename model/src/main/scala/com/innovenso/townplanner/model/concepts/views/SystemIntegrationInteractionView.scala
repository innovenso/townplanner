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

case class SystemIntegrationInteractionView(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    forSystemIntegration: Key,
    title: String = "System Integration Interaction View",
    withStepCounter: Boolean = true,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends View
    with HasDescription
    with HasInteractions
    with HasExternalIds
    with HasLinks {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "System Integration Interaction View"
  )
  val pointInTime: ADay = Today
  val layer: Layer = ApplicationLayer

  def withProperty(property: Property): SystemIntegrationInteractionView =
    copy(properties = this.properties + (property.key -> property))
}

object SystemIntegrationInteractionView {
  def apply(
      forSystemIntegration: ItSystemIntegration
  ) = new SystemIntegrationInteractionView(
    forSystemIntegration = forSystemIntegration.key
  )
}

trait HasSystemIntegrationInteractionViews
    extends HasViews
    with HasTechnologies {
  def systemIntegrationInteractionViews
      : List[CompiledSystemIntegrationInteractionView] =
    components(classOf[SystemIntegrationInteractionView]).map(
      SystemIntegrationInteractionViewCompiler(_, this).compile
    )
  def systemIntegrationInteractionView(
      key: Key
  ): Option[CompiledSystemIntegrationInteractionView] =
    component(key, classOf[SystemIntegrationInteractionView]).map(
      SystemIntegrationInteractionViewCompiler(_, this).compile
    )
}

trait CanAddSystemIntegrationInteractionViews
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      systemIntegrationInteractionView: SystemIntegrationInteractionView
  ): SystemIntegrationInteractionView =
    has(systemIntegrationInteractionView)
}

case class CompiledSystemIntegrationInteractionView(
    view: SystemIntegrationInteractionView,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[SystemIntegrationInteractionView]
    with HasItSystemIntegrations
    with HasItSystems
    with HasItContainers
    with HasItPlatforms
    with HasRelationships
    with HasBusinessActors
    with HasTechnologies {
  def otherSystems: List[ItSystem] = systems.filterNot(systemContexts.toSet)

  def systemContexts: List[ItSystem] =
    containers.map(system).filter(_.nonEmpty).map(_.get)

  def withStepCounter: Boolean = view.withStepCounter

  def steps: List[(Interaction, Int)] =
    interactions.zip(LazyList from 1)

  def integration: Option[ItSystemIntegration] = systemIntegrations.headOption

  def interactions: List[Interaction] =
    integration.map(it => it.interactions).getOrElse(Nil)

  def stepSource(step: Interaction): Element =
    component(step.source, classOf[Element]).get

  def stepTarget(step: Interaction): Element =
    component(step.target, classOf[Element]).get
}

case class SystemIntegrationInteractionViewCompiler(
    view: SystemIntegrationInteractionView,
    source: HasSystemIntegrationInteractionViews
) extends ViewCompiler[
      SystemIntegrationInteractionView,
      CompiledSystemIntegrationInteractionView,
      HasSystemIntegrationInteractionViews
    ] {
  def compile: CompiledSystemIntegrationInteractionView =
    CompiledSystemIntegrationInteractionView(
      view,
      viewTitle,
      groupTitle(view.forSystemIntegration),
      viewComponents(
        integration.toSet ++ elementsIncludingSystemContexts ++ compositions ++ technologies ++ implementingTechnologies
      )
    )

  private def integration: Option[ItSystemIntegration] =
    source.component(view.forSystemIntegration, classOf[ItSystemIntegration])

  private def interactions: List[Interaction] =
    integration.map(it => it.interactions).getOrElse(Nil)

  private def elements: List[Element] =
    interactions
      .flatMap(interaction => List(interaction.source, interaction.target))
      .distinct
      .map(source.component(_, classOf[Element]))
      .filter(_.nonEmpty)
      .map(_.get)

  private def systemContexts: List[ItSystem] = elements.flatMap(element =>
    source
      .relationships(element, classOf[Composition], classOf[ItSystem])
      .flatMap(rel =>
        source.relationshipParticipantsOfType(rel, classOf[ItSystem])
      )
  )

  private def containers: Set[ItContainer] = elements
    .filter(_.isInstanceOf[ItContainer])
    .map(_.asInstanceOf[ItContainer])
    .toSet

  private def technologies: Set[Technology] =
    containers.flatMap(source.technologies(_))

  private def elementsIncludingSystemContexts: Set[Element] =
    elements.toSet ++ systemContexts.toSet

  private def compositions: Set[Composition] =
    elementsIncludingSystemContexts.flatMap(element =>
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
