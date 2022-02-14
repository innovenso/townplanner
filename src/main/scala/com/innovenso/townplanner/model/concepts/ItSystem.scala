package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  CanConfigureArchitectureVerdict,
  CanConfigureCriticality,
  CanConfigureDescription,
  CanConfigureExternalIds,
  CanConfigureFatherTime,
  CanConfigureLinks,
  CanConfigureResilienceMeasures,
  CanConfigureSWOT,
  HasArchitectureVerdict,
  HasCriticality,
  HasDescription,
  HasExternalIds,
  HasFatherTime,
  HasLinks,
  HasResilienceMeasures,
  HasSWOT,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class ItSystem(
    key: Key = Key(),
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
    with CanBeFlowSource
    with CanBeFlowTarget
    with CanTrigger
    with CanBeTriggered
    with CanRealize
    with CanBeAssociated
    with CanCompose
    with CanBeComposedOf
    with CanBeImplemented
    with CanImplement
    with CanBeImpacted
    with CanBeDelivered {
  val layer: Layer = ApplicationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "IT System"
  )

  def withProperty(property: Property): ItSystem =
    copy(properties = this.properties + (property.key -> property))
}

trait HasItSystems extends HasModelComponents with HasRelationships {
  def systems: List[ItSystem] = components(classOf[ItSystem])
  def system(key: Key): Option[ItSystem] = component(key, classOf[ItSystem])
  def platformSystems(itPlatform: ItPlatform): List[ItSystem] =
    directOutgoingDependencies(
      itPlatform,
      classOf[Composition],
      classOf[ItSystem]
    )
  def systemPlatform(itSystem: ItSystem): Option[ItPlatform] =
    directIncomingDependencies(
      itSystem,
      classOf[Composition],
      classOf[ItPlatform]
    ).headOption

  def realizedArchitectureBuildingBlocks(
      itSystem: ItSystem
  ): List[ArchitectureBuildingBlock] = directOutgoingDependencies(
    itSystem,
    classOf[Realization],
    classOf[ArchitectureBuildingBlock]
  )
  def realizingSystems(
      architectureBuildingBlock: ArchitectureBuildingBlock
  ): List[ItSystem] = directIncomingDependencies(
    architectureBuildingBlock,
    classOf[Realization],
    classOf[ItSystem]
  )
}

case class ItSystemConfigurer(
    modelComponent: ItSystem,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[ItSystem]
    with CanConfigureLinks[ItSystem]
    with CanConfigureExternalIds[ItSystem]
    with CanConfigureSWOT[ItSystem]
    with CanConfigureFatherTime[ItSystem]
    with CanConfigureCompositionSource[ItSystem]
    with CanConfigureCompositionTarget[ItSystem]
    with CanConfigureArchitectureVerdict[ItSystem]
    with CanConfigureCriticality[ItSystem]
    with CanConfigureResilienceMeasures[ItSystem]
    with CanConfigureFlowSource[ItSystem]
    with CanConfigureFlowTarget[ItSystem]
    with CanConfigureTriggerSource[ItSystem]
    with CanConfigureTriggerTarget[ItSystem]
    with CanConfigureAssociations[ItSystem]
    with CanConfigureRealizationSource[ItSystem]
    with CanConfigureImplementationTarget[ItSystem]
    with CanConfigureImplementationSource[ItSystem]
    with CanConfigureDeliveryTarget[ItSystem] {
  def as(
      body: ItSystemConfigurer => Any
  ): ItSystem = {
    body.apply(this)
    propertyAdder.townPlan
      .system(modelComponent.key)
      .get
  }
}

trait CanAddItSystems extends CanAddProperties with CanAddRelationships {
  def describes(system: ItSystem): ItSystemConfigurer =
    ItSystemConfigurer(has(system), this, this)
}
