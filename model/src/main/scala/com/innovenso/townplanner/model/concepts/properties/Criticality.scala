package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

abstract class Criticality extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = false
  val sortKey: SortKey = SortKey.next
  def name: String
  def description: String
  def consequences: String

  def level: Char
}

case class Catastrophic(consequences: String = "") extends Criticality {
  val name = "Catastrophic"
  val level = 'A'
  val description =
    "a component failure would significantly affect the operability of the system/platform and could cause bankruptcy, law suits or even the loss of life"
}

case class Hazardous(consequences: String = "") extends Criticality {
  val name = "Hazardous"
  val level = 'B'
  val description =
    "a failure in these systems would pose a major threat to the company and its employees"
}

case class Major(consequences: String = "") extends Criticality {
  val name = "Major"
  val level = 'C'
  val description =
    "a failure in these systems will impact the working of the larger whole, resulting in outages and loss of money"
}

case class Minor(consequences: String = "") extends Criticality {
  val name = "Minor"
  val level = 'D'
  val description =
    "a failure in these systems will have a minor impact the working of the larger whole, not resulting in system outages, but a reduced customer experience"
}

case class NoEffect(consequences: String = "") extends Criticality {
  val name = "No Effect"
  val level = 'E'
  val description =
    "a failure in these systems will have no impact the working of the larger whole, and will not have any impact on the customer experience"
}

case class UnknownCriticality(consequences: String = "") extends Criticality {
  val name = "Unknown"
  val level = 'X'
  val description =
    "The criticality of this component has not yet been determined"
}

trait HasCriticality extends HasProperties {
  def isCatastrophicCriticality: Boolean =
    criticality.isInstanceOf[Catastrophic]

  def isHazardousCriticality: Boolean = criticality.isInstanceOf[Hazardous]

  def isMajorCriticality: Boolean = criticality.isInstanceOf[Major]

  def isMinorCriticality: Boolean = criticality.isInstanceOf[Minor]

  def criticality: Criticality =
    props(classOf[Criticality]).headOption
      .getOrElse(UnknownCriticality())

  def isNoEffectCriticality: Boolean = criticality.isInstanceOf[NoEffect]

  def isUnknownCriticality: Boolean =
    criticality.isInstanceOf[UnknownCriticality]

}

trait CanConfigureCriticality[
    ModelComponentType <: HasCriticality
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def ratesFailureAs(criticality: Criticality): ModelComponentType =
    propertyAdder.withProperty(modelComponent, criticality)
}
