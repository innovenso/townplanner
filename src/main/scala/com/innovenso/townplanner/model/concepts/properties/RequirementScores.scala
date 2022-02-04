package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Description, Key, SortKey}

case class RequirementScore(
    requirementKey: Key,
    description: Description = Description(None),
    scoreWeight: ScoreWeight = UnknownScore
) extends Property {
  val canBePlural: Boolean = true
  val key: Key = Key()
  val sortKey: SortKey = SortKey(None)
}

trait HasRequirementScores extends HasProperties {
  def scores: List[RequirementScore] = props(classOf[RequirementScore])
  def score(requirementKey: Key): RequirementScore =
    props(classOf[RequirementScore])
      .find(s => s.requirementKey == requirementKey)
      .getOrElse(
        RequirementScore(
          requirementKey,
          Description(Some("not yet specified")),
          UnknownScore
        )
      )
}

trait CanAddRequirementScores extends CanAddProperties {
  def withRequirementScore[ModelComponentType <: HasRequirementScores](
      modelComponent: ModelComponentType,
      requirementsHolderKey: Key,
      requirementKey: Key,
      description: Description = Description(None),
      weight: ScoreWeight
  ): ModelComponentType = {
    if (
      !townPlan
        .component(
          requirementsHolderKey,
          classOf[HasRequirements]
        )
        .exists(_.requirements.exists(_.key == requirementKey))
    )
      throw new IllegalArgumentException(
        s"${requirementsHolderKey} does not have requirement ${requirementKey}"
      )
    else
      withProperty(
        modelComponent,
        RequirementScore(
          requirementKey = requirementKey,
          description = description,
          scoreWeight = weight
        )
      )
  }
}

trait ScoreWeight {
  def name: String
  def weight: Int
}

case object ExceedsExpectations extends ScoreWeight {
  val name: String = "Exceeds expectations"
  val weight: Int = 5
}

case object MeetsExpectations extends ScoreWeight {
  val name: String = "Meets expectations"
  val weight: Int = 3
}

case object AlmostMeetsExpectations extends ScoreWeight {
  val name: String = "Almost meets expectations"
  val weight: Int = 1
}

case object DoesNotMeetExpectations extends ScoreWeight {
  val name: String = "Does not meet expectations"
  val weight: Int = 0
}

case object UnknownScore extends ScoreWeight {
  val name: String = "Unknown"
  val weight: Int = 1
}
