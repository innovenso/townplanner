package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

abstract class SWOT extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = true
  val sortKey: SortKey = SortKey.next
  def name: String
  def description: String
}

case class Strength(description: String) extends SWOT {
  val name = "Strength"
}

case class Weakness(description: String) extends SWOT {
  val name = "Weakness"
}

case class Opportunity(description: String) extends SWOT {
  val name = "Opportunity"
}

case class Threat(description: String) extends SWOT {
  val name = "Threat"
}

trait HasSWOT extends HasProperties {
  def swots: List[SWOT] =
    props(classOf[SWOT])
  def strengths: List[SWOT] =
    props(classOf[Strength])
  def weaknesses: List[Weakness] =
    props(classOf[Weakness])
  def opportunities: List[Opportunity] =
    props(classOf[Opportunity])
  def threats: List[Threat] =
    props(classOf[Threat])

}

trait CanConfigureSWOT[
    ModelComponentType <: HasSWOT
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(swot: SWOT): ModelComponentType =
    propertyAdder.withProperty(modelComponent, swot)
}
