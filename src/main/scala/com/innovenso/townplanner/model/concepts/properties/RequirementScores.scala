package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

sealed trait RequirementScore extends Property {
  val canBePlural: Boolean = true
  val key: Key = Key()
  val sortKey: SortKey = SortKey(None)
  def name: String
  def weight: Int
  def requirementKey: Key
  def description: String
}

case class ExceedsExpectations(
    requirementKey: Key = Key(),
    description: String = ""
) extends RequirementScore {
  val name: String = "Exceeds expectations"
  val weight: Int = 5
}

case class MeetsExpectations(
    requirementKey: Key = Key(),
    description: String = ""
) extends RequirementScore {
  val name: String = "Meets expectations"
  val weight: Int = 3
}

case class AlmostMeetsExpectations(
    requirementKey: Key = Key(),
    description: String = ""
) extends RequirementScore {
  val name: String = "Almost meets expectations"
  val weight: Int = 1
}

case class DoesNotMeetExpectations(
    requirementKey: Key = Key(),
    description: String = ""
) extends RequirementScore {
  val name: String = "Does not meet expectations"
  val weight: Int = 0
}

case class UnknownScore(
    requirementKey: Key = Key(),
    description: String = ""
) extends RequirementScore {
  val name: String = "Unknown"
  val weight: Int = 1
}

trait HasRequirementScores extends HasProperties {
  def scores: List[RequirementScore] = props(classOf[RequirementScore])
  def score(requirementKey: Key): RequirementScore =
    props(classOf[RequirementScore])
      .find(s => s.requirementKey == requirementKey)
      .getOrElse(
        UnknownScore(
          requirementKey,
          "not yet specified"
        )
      )
}

case class RequirementScoreConfigurer(
    modelComponent: HasRequirementScores,
    requirementScore: RequirementScore,
    propertyAdder: CanAddRequirementScores
) {
  def on(
      requirementHolderKey: Key,
      requirementKey: Key
  ): RequirementScore = {
    val configuredScore = requirementScore match {
      case exceedsExpectations: ExceedsExpectations =>
        exceedsExpectations.copy(requirementKey = requirementKey)
      case meetsExpectations: MeetsExpectations =>
        meetsExpectations.copy(requirementKey = requirementKey)
      case almostMeetsExpectations: AlmostMeetsExpectations =>
        almostMeetsExpectations.copy(requirementKey = requirementKey)
      case doesNotMeetExpectations: DoesNotMeetExpectations =>
        doesNotMeetExpectations.copy(requirementKey = requirementKey)
      case unknownScore: UnknownScore =>
        unknownScore.copy(requirementKey = requirementKey)
    }
    propertyAdder.withRequirementScore(
      modelComponent,
      requirementHolderKey,
      configuredScore
    )
    configuredScore
  }
}

trait CanConfigureRequirementScores[
    ModelComponentType <: HasRequirementScores
] {
  def propertyAdder: CanAddRequirementScores
  def modelComponent: ModelComponentType

  def scores(requirementScore: RequirementScore): RequirementScoreConfigurer = {
    RequirementScoreConfigurer(modelComponent, requirementScore, propertyAdder)
  }
}

trait CanAddRequirementScores extends CanAddProperties {
  def withRequirementScore[ModelComponentType <: HasRequirementScores](
      modelComponent: ModelComponentType,
      requirementsHolderKey: Key,
      requirementScore: RequirementScore
  ): ModelComponentType = {
    if (
      !townPlan
        .component(
          requirementsHolderKey,
          classOf[HasRequirements]
        )
        .exists(_.requirements.exists(_.key == requirementScore.requirementKey))
    )
      throw new IllegalArgumentException(
        s"${requirementsHolderKey} does not have requirement ${requirementScore.requirementKey}"
      )
    else {
      withProperty(
        modelComponent,
        requirementScore
      )
    }
  }
}
