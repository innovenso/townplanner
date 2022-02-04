package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class Decision(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title,
    description: Description = Description(None),
    status: DecisionStatus = NotStarted,
    outcome: Description = Description(None),
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDocumentation
    with CanImpact
    with CanServe
    with CanBeAssociated
    with CanBeInfluenced
    with CanHaveRaci
    with CanHaveStakeholder
    with CanBeTriggered
    with HasRequirements
    with HasContext {
  val layer: Layer = ImplementationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType("Decision")

  def withProperty(property: Property): Decision =
    copy(properties = this.properties + (property.key -> property))
}

case class DecisionOption(
    key: Key = Key(),
    decisionKey: Key,
    sortKey: SortKey = SortKey(None),
    title: Title,
    description: Description = Description(None),
    verdict: DecisionOptionVerdict,
    outcome: Description = Description(None),
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDocumentation
    with HasRequirementScores
    with HasCosts
    with CanBeAssociated {
  val layer: Layer = ImplementationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Decision Option"
  )

  def withProperty(property: Property): DecisionOption =
    copy(properties = this.properties + (property.key -> property))
}

trait HasDecisions extends HasModelComponents with HasRelationships {
  def decisions: List[Decision] = components(classOf[Decision])

  def functionalRequirementScores(
      decisionOption: DecisionOption
  ): List[(Requirement, RequirementScore)] =
    scores(decisionOption).filter(_._1.isInstanceOf[FunctionalRequirement])

  def scores(
      decisionOption: DecisionOption
  ): List[(Requirement, RequirementScore)] =
    decision(decisionOption.decisionKey)
      .map(_.requirements.map(r => (r, decisionOption.score(r.key))))
      .getOrElse(Nil)

  def decision(key: Key): Option[Decision] = component(key, classOf[Decision])

  def qualityAttributeRequirementScores(
      decisionOption: DecisionOption
  ): List[(Requirement, RequirementScore)] = scores(decisionOption).filter(
    _._1.isInstanceOf[QualityAttributeRequirement]
  )

  def constraintScores(
      decisionOption: DecisionOption
  ): List[(Requirement, RequirementScore)] =
    scores(decisionOption).filter(_._1.isInstanceOf[Constraint])

  def optionsUnderInvestigation(decision: Decision): List[DecisionOption] =
    options(decision).filter(_.verdict == UnderInvestigation)

  def chosenOptions(decision: Decision): List[DecisionOption] =
    options(decision).filter(_.verdict == Chosen)

  def options(decision: Decision): List[DecisionOption] = components(
    classOf[DecisionOption]
  ).filter(_.decisionKey == decision.key)
    .map(o =>
      if (isDecisionOptionRejected(o)) o.copy(verdict = Rejected) else o
    )

  private def isDecisionOptionRejected(
      decisionOption: DecisionOption
  ): Boolean = scores(decisionOption).exists(rr =>
    rr._1.weight == MustHave && rr._2.scoreWeight == DoesNotMeetExpectations
  )

  def rejectedOptions(decision: Decision): List[DecisionOption] =
    options(decision).filter(_.verdict == Rejected)
}

sealed trait DecisionStatus {
  def name: String
}

case object InProgress extends DecisionStatus {
  val name: String = "In progress"
}

case object NotStarted extends DecisionStatus {
  val name: String = "Not started"
}

case object Decided extends DecisionStatus {
  val name: String = "Decided"
}

sealed trait DecisionOptionVerdict {
  def name: String
}

case object UnderInvestigation extends DecisionOptionVerdict {
  val name = "Under investigation"
}

case object Chosen extends DecisionOptionVerdict {
  val name = "Chosen"
}

case object Rejected extends DecisionOptionVerdict {
  val name = "Rejected"
}
