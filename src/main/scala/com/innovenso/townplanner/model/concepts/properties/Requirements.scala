package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Description, Key, SortKey, Title}

case class Constraint(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title,
    description: Description = Description(None),
    weight: RequirementWeight
) extends Requirement
case class FunctionalRequirement(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title,
    description: Description = Description(None),
    weight: RequirementWeight
) extends Requirement
case class QualityAttributeRequirement(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title,
    sourceOfStimulus: Description = Description(None),
    stimulus: Description = Description(None),
    environment: Description = Description(None),
    response: Description = Description(None),
    responseMeasure: Description = Description(None),
    weight: RequirementWeight
) extends Requirement {
  val description: Description = response
}

trait Requirement extends Property {
  val canBePlural: Boolean = true
  def title: Title
  def description: Description
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
