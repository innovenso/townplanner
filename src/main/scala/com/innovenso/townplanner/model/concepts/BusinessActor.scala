package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  HasDocumentation,
  Property
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class BusinessActor(
    key: Key,
    sortKey: SortKey,
    title: Title,
    description: Description,
    businessActorType: BusinessActorType,
    properties: Map[Key, Property]
) extends Element
    with HasDocumentation
    with CanBeFlowSource
    with CanBeFlowTarget
    with CanTrigger
    with CanDeliver
    with CanInfluence
    with CanServe
    with CanBeAssociated
    with CanBeStakeholder {
  val layer: Layer = BusinessLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Business Actor"
  )

  def withProperty(property: Property): BusinessActor =
    copy(properties = this.properties + (property.key -> property))
}

trait HasBusinessActors
    extends HasModelComponents
    with HasEnterprises
    with HasRelationships {
  def businessActors(
      businessActorType: BusinessActorType
  ): List[BusinessActor] =
    businessActors.filter(a => a.businessActorType == businessActorType)

  def businessActors: List[BusinessActor] = components(
    classOf[BusinessActor]
  )

  def businessActor(key: Key): Option[BusinessActor] =
    component(key, classOf[BusinessActor])
  def enterprise(businessActor: BusinessActor): Option[Enterprise] =
    directOutgoingDependencies(
      businessActor,
      Serves,
      classOf[Enterprise]
    ).headOption
}

sealed trait BusinessActorType {
  def name: String
}

case object NounActor extends BusinessActorType {
  val name: String = "Noun"
}

case object IndividualActor extends BusinessActorType {
  val name: String = "Individual"
}

case object OrganisationActor extends BusinessActorType {
  val name: String = "Organisation"
}

case object TeamActor extends BusinessActorType {
  val name: String = "Team"
}
