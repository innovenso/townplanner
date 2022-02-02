package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.{CanManipulateTownPlan, TownPlan}
import com.innovenso.townplanner.model.concepts.properties.{
  HasDocumentation,
  Property
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta.{
  ActiveStructure,
  Aspect,
  Behavior,
  BusinessLayer,
  Description,
  Key,
  Layer,
  ModelComponentType,
  PassiveStructure,
  SortKey,
  StrategyLayer,
  Title
}

import scala.util.{Success, Try}

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
  def businessActors: List[BusinessActor] = components(
    classOf[BusinessActor]
  )
  def businessActors(
      businessActorType: BusinessActorType
  ): List[BusinessActor] =
    businessActors.filter(a => a.businessActorType == businessActorType)
  def businessActor(key: Key): Option[BusinessActor] =
    component(key, classOf[BusinessActor])
  def enterprise(businessActor: BusinessActor): Option[Enterprise] =
    directOutgoingDependencies(
      businessActor,
      Serves,
      classOf[Enterprise]
    ).headOption
}

trait CanAddBusinessActors
    extends CanManipulateTownPlan
    with CanAddRelationships {
  def withBusinessActor(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None),
      businessActorType: BusinessActorType
  ): Try[(TownPlan, BusinessActor)] = withNewModelComponent(
    BusinessActor(
      key = key,
      sortKey = sortKey,
      title = title,
      description = description,
      businessActorType = businessActorType,
      properties = Map.empty[Key, Property]
    )
  )

  def withEnterpriseBusinessActor(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None),
      enterprise: Enterprise,
      businessActorType: BusinessActorType
  ): Try[(TownPlan, BusinessActor)] =
    withBusinessActor(key, sortKey, title, description, businessActorType)
      .flatMap(tb =>
        withRelationship(
          title = Title(""),
          relationshipType = Serves,
          sourceKey = tb._2.key,
          targetKey = enterprise.key
        ).flatMap(tr => Success((tr._1, tb._2)))
      )

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
