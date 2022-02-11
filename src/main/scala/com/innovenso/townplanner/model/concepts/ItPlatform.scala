package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  CanConfigureArchitectureVerdict,
  CanConfigureCriticality,
  CanConfigureDescription,
  CanConfigureLinks,
  CanConfigureResilienceMeasures,
  HasArchitectureVerdict,
  HasCriticality,
  HasDescription,
  HasLinks,
  HasResilienceMeasures,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class ItPlatform(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDescription
    with HasLinks
    with HasArchitectureVerdict
    with HasCriticality
    with HasResilienceMeasures
    with CanBeFlowSource
    with CanBeFlowTarget
    with CanTrigger
    with CanBeTriggered
    with CanRealize
    with CanBeAssociated
    with CanBeComposedOf
    with CanBeImpacted
    with CanBeImplemented {
  val layer: Layer = ApplicationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "IT Platform"
  )

  def withProperty(property: Property): ItPlatform =
    copy(properties = this.properties + (property.key -> property))
}

trait HasItPlatforms extends HasModelComponents with HasRelationships {
  def platforms: List[ItPlatform] = components(classOf[ItPlatform])
  def platform(key: Key): Option[ItPlatform] =
    component(key, classOf[ItPlatform])
  def realizedArchitectureBuildingBlocks(
      itPlatform: ItPlatform
  ): List[ArchitectureBuildingBlock] = directOutgoingDependencies(
    itPlatform,
    classOf[Realization],
    classOf[ArchitectureBuildingBlock]
  )
  def realizingPlatforms(
      architectureBuildingBlock: ArchitectureBuildingBlock
  ): List[ItPlatform] = directIncomingDependencies(
    architectureBuildingBlock,
    classOf[Realization],
    classOf[ItPlatform]
  )
}

case class ItPlatformConfigurer(
    modelComponent: ItPlatform,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[ItPlatform]
    with CanConfigureCompositionSource[ItPlatform]
    with CanConfigureArchitectureVerdict[ItPlatform]
    with CanConfigureLinks[ItPlatform]
    with CanConfigureCriticality[ItPlatform]
    with CanConfigureResilienceMeasures[ItPlatform]
    with CanConfigureFlowSource[ItPlatform]
    with CanConfigureFlowTarget[ItPlatform]
    with CanConfigureTriggerSource[ItPlatform]
    with CanConfigureTriggerTarget[ItPlatform]
    with CanConfigureAssociations[ItPlatform]
    with CanConfigureRealizationSource[ItPlatform]
    with CanConfigureImplementationTarget[ItPlatform] {
  def as(
      body: ItPlatformConfigurer => Any
  ): ItPlatform = {
    body.apply(this)
    propertyAdder.townPlan
      .platform(modelComponent.key)
      .get
  }
}

trait CanAddItPlatforms extends CanAddProperties with CanAddRelationships {
  def describes(platform: ItPlatform): ItPlatformConfigurer =
    ItPlatformConfigurer(has(platform), this, this)
}
