package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

case class Constraint(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: String,
    description: String = "",
    weight: RequirementWeight = ShouldHave
) extends Requirement
case class FunctionalRequirement(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: String,
    description: String = "",
    weight: RequirementWeight = ShouldHave
) extends Requirement

case class QualityAttributeRequirement(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: String,
    sourceOfStimulus: String = "",
    stimulus: String = "",
    environment: String = "",
    response: String = "",
    responseMeasure: String = "",
    weight: RequirementWeight = ShouldHave
) extends Requirement {
  val description: String = response
}

trait Requirement extends Property {
  val canBePlural: Boolean = true
  def title: String
  def description: String
  def weight: RequirementWeight
}

trait HasRequirements extends HasProperties {
  def requirements: List[Requirement] = props(classOf[Requirement])
  def constraints: List[Constraint] = props(classOf[Constraint])
  def functionalRequirements: List[FunctionalRequirement] = props(
    classOf[FunctionalRequirement]
  )
  def qualityAttributeRequirements: List[QualityAttributeRequirement] = props(
    classOf[QualityAttributeRequirement]
  )
}

trait CanConfigureRequirements[
    ModelComponentType <: HasCosts
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(requirement: Requirement): ModelComponentType =
    propertyAdder.withProperty(modelComponent, requirement)
}

trait RequirementWeight {
  def name: String
  def weight: Int
}

case object MustHave extends RequirementWeight {
  val name: String = "Must Have"
  val weight: Int = 21
}

case object ShouldHave extends RequirementWeight {
  val name: String = "Should Have"
  val weight: Int = 13
}

case object CouldHave extends RequirementWeight {
  val name: String = "Could Have"
  val weight: Int = 8
}

case object WouldHave extends RequirementWeight {
  val name: String = "Would Have"
  val weight: Int = 5
}

case object UnknownRequirementWeight extends RequirementWeight {
  val name: String = "Unknown"
  val weight: Int = 1
}
