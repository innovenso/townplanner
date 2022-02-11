package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  CanConfigureDescription,
  CanConfigureLinks,
  CanConfigureSWOT,
  HasDescription,
  HasLinks,
  HasSWOT,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

sealed trait Principle
    extends Element
    with HasDescription
    with HasLinks
    with HasSWOT
    with CanServe
    with CanInfluence {
  val modelComponentType: ModelComponentType = ModelComponentType("Principle")
  val aspect: Aspect = ActiveStructure
  val layer: Layer = MotivationLayer
  val sortKey: SortKey = SortKey.next
  def principleType: String
}

case class CorporatePrinciple(
    key: Key = Key(),
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Principle {
  val principleType: String = "Corporate Principle"

  def withProperty(property: Property): CorporatePrinciple =
    copy(properties = this.properties + (property.key -> property))
}

case class DesignPrinciple(
    key: Key = Key(),
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Principle {
  val principleType: String = "Design Principle"

  def withProperty(property: Property): DesignPrinciple =
    copy(properties = this.properties + (property.key -> property))
}

trait HasPrinciples extends HasModelComponents {
  def principles: List[Principle] = components(classOf[Principle])
  def corporatePrinciples: List[Principle] = components(
    classOf[CorporatePrinciple]
  )
  def designPrinciples: List[Principle] = components(classOf[DesignPrinciple])
  def principle(key: Key): Option[Principle] =
    component(key, classOf[Principle])
}

case class PrincipleConfigurer[PrincipleType <: Principle](
    modelComponent: PrincipleType,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[Principle]
    with CanConfigureLinks[Principle]
    with CanConfigureSWOT[Principle]
    with CanConfigureServingSource[Principle]
    with CanConfigureInfluenceSource[Principle] {
  def as(
      body: PrincipleConfigurer[PrincipleType] => Any
  ): PrincipleType = {
    body.apply(this)
    propertyAdder.townPlan
      .component(modelComponent.key, modelComponent.getClass)
      .get
  }
}

trait CanAddPrinciples extends CanAddProperties with CanAddRelationships {
  def describes(
      principle: CorporatePrinciple
  ): PrincipleConfigurer[CorporatePrinciple] =
    PrincipleConfigurer(has(principle), this, this)
  def describes(
      principle: DesignPrinciple
  ): PrincipleConfigurer[DesignPrinciple] =
    PrincipleConfigurer(has(principle), this, this)
}
