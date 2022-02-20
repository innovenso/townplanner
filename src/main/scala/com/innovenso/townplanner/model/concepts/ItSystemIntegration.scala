package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class ItSystemIntegration(
    key: Key = Key(),
    source: Key = Key(),
    target: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDescription
    with HasArchitectureVerdict
    with HasCriticality
    with HasLinks
    with HasExternalIds
    with HasSWOT
    with HasFatherTime
    with HasResilienceMeasures
    with HasThroughput
    with HasInteractions
    with CanBeAssociated
    with CanBeImplemented
    with CanBeImpacted
    with CanBeDelivered {
  val layer: Layer = ApplicationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "IT System Integration"
  )

  def withProperty(property: Property): ItSystemIntegration =
    copy(properties = this.properties + (property.key -> property))

  def hasSystem(system: ItSystem): Boolean =
    source == system.key || target == system.key
}

trait HasItSystemIntegrations extends HasModelComponents with HasRelationships {
  def systemIntegration(key: Key): Option[ItSystemIntegration] =
    component(key, classOf[ItSystemIntegration])

  def systemIntegrations(system: ItSystem): List[ItSystemIntegration] =
    systemIntegrations.filter(_.hasSystem(system))

  def systemIntegrations(
      source: ItSystem,
      target: ItSystem
  ): List[ItSystemIntegration] = systemIntegrations.filter(it =>
    it.hasSystem(source) && it.hasSystem(target)
  )

  def systemIntegrations: List[ItSystemIntegration] = components(
    classOf[ItSystemIntegration]
  )
}

case class ItSystemIntegrationConfigurer(
    modelComponent: ItSystemIntegration,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[ItSystemIntegration]
    with CanConfigureArchitectureVerdict[ItSystemIntegration]
    with CanConfigureCriticality[ItSystemIntegration]
    with CanConfigureLinks[ItSystemIntegration]
    with CanConfigureExternalIds[ItSystemIntegration]
    with CanConfigureSWOT[ItSystemIntegration]
    with CanConfigureFatherTime[ItSystemIntegration]
    with CanConfigureResilienceMeasures[ItSystemIntegration]
    with CanConfigureThroughput[ItSystemIntegration]
    with CanConfigureInteractions[ItSystemIntegration]
    with CanConfigureAssociations[ItSystemIntegration]
    with CanConfigureImplementationTarget[ItSystemIntegration]
    with CanConfigureDeliveryTarget[ItSystemIntegration] {
  def between(system: ItSystem): ItSystemIntegrationConfigurer =
    copy(modelComponent = modelComponent.copy(source = system.key))
  def and(system: ItSystem): ItSystemIntegrationConfigurer =
    copy(modelComponent = modelComponent.copy(target = system.key))
  def as(
      body: ItSystemIntegrationConfigurer => Any
  ): ItSystemIntegration = {
    propertyAdder.has(modelComponent)
    body.apply(this)
    propertyAdder.townPlan
      .systemIntegration(modelComponent.key)
      .get
  }
}

trait CanAddItSystemIntegrations
    extends CanAddProperties
    with CanAddRelationships {
  def describes(
      integration: ItSystemIntegration
  ): ItSystemIntegrationConfigurer =
    ItSystemIntegrationConfigurer(integration, this, this)
}
