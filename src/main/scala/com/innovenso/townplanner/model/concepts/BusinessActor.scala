package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  CanConfigureDescription,
  CanConfigureLinks,
  HasDescription,
  HasLinks,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

sealed trait BusinessActor
    extends Element
    with HasDescription
    with HasLinks
    with CanBeFlowSource
    with CanBeFlowTarget
    with CanTrigger
    with CanDeliver
    with CanInfluence
    with CanServe
    with CanBeAssociated
    with CanBeStakeholder
    with CanBeRaci {
  val layer: Layer = BusinessLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Business Actor"
  )
}

case class ActorNoun(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends BusinessActor {
  def withProperty(property: Property): ActorNoun =
    copy(properties = this.properties + (property.key -> property))
}

case class IndividualActor(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends BusinessActor {
  def withProperty(property: Property): IndividualActor =
    copy(properties = this.properties + (property.key -> property))
}

case class OrganisationActor(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends BusinessActor {
  def withProperty(property: Property): OrganisationActor =
    copy(properties = this.properties + (property.key -> property))
}

case class TeamActor(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends BusinessActor {
  def withProperty(property: Property): TeamActor =
    copy(properties = this.properties + (property.key -> property))
}

trait HasBusinessActors
    extends HasModelComponents
    with HasEnterprises
    with HasRelationships {
  def actorNouns: List[BusinessActor] =
    businessActors.filter(a => a.isInstanceOf[ActorNoun])
  def individualActors: List[BusinessActor] =
    businessActors.filter(a => a.isInstanceOf[IndividualActor])
  def organisationActors: List[BusinessActor] =
    businessActors.filter(a => a.isInstanceOf[OrganisationActor])
  def teamActors: List[BusinessActor] =
    businessActors.filter(a => a.isInstanceOf[TeamActor])

  def businessActors: List[BusinessActor] = components(
    classOf[BusinessActor]
  )

  def businessActor(key: Key): Option[BusinessActor] =
    component(key, classOf[BusinessActor])

  def enterprise(businessActor: BusinessActor): Option[Enterprise] =
    directOutgoingDependencies(
      businessActor,
      classOf[Serving],
      classOf[Enterprise]
    ).headOption
}

case class BusinessActorConfigurer[BusinessActorType <: BusinessActor](
    modelComponent: BusinessActorType,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[BusinessActorType]
    with CanConfigureLinks[BusinessActorType]
    with CanConfigureServingSource[BusinessActorType]
    with CanConfigureFlowSource[BusinessActorType]
    with CanConfigureFlowTarget[BusinessActorType]
    with CanConfigureTriggerSource[BusinessActorType]
    with CanConfigureAssociations[BusinessActorType]
    with CanConfigureDeliverySource[BusinessActorType]
    with CanConfigureRaciSource[BusinessActorType]
    with CanConfigureInfluenceSource[BusinessActorType]
    with CanConfigureStakeholderSource[BusinessActorType] {
  def as(
      body: BusinessActorConfigurer[BusinessActorType] => Any
  ): BusinessActorType = {
    body.apply(this)
    propertyAdder.townPlan
      .component(modelComponent.key, modelComponent.getClass)
      .get
  }
}

trait CanAddBusinessActors extends CanAddProperties with CanAddRelationships {
  def describes(
      businessActor: IndividualActor
  ): BusinessActorConfigurer[IndividualActor] =
    BusinessActorConfigurer(
      has(businessActor),
      this,
      this
    )
  def describes(
      businessActor: ActorNoun
  ): BusinessActorConfigurer[ActorNoun] =
    BusinessActorConfigurer(
      has(businessActor),
      this,
      this
    )
  def describes(
      businessActor: TeamActor
  ): BusinessActorConfigurer[TeamActor] =
    BusinessActorConfigurer(
      has(businessActor),
      this,
      this
    )
  def describes(
      businessActor: OrganisationActor
  ): BusinessActorConfigurer[OrganisationActor] =
    BusinessActorConfigurer(
      has(businessActor),
      this,
      this
    )

}
