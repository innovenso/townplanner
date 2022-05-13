package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class Decision(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    status: DecisionStatus = NotStarted,
    outcome: String = "",
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDescription
    with HasLinks
    with HasFatherTime
    with HasDataProtectionConcerns
    with HasSecurityImpact
    with CanImpact
    with CanServe
    with CanBeAssociated
    with CanBeInfluenced
    with CanBeComposedOf
    with CanHaveRaci
    with CanHaveStakeholder
    with CanBeTriggered
    with HasRequirements
    with HasContext {
  val layer: Layer = MotivationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType("Decision")

  def withProperty(property: Property): Decision =
    copy(properties = this.properties + (property.key -> property))
}

case class DecisionOption(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    verdict: DecisionOptionVerdict = UnderInvestigation(),
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDescription
    with HasLinks
    with HasDataProtectionConcerns
    with HasRequirementScores
    with HasCosts
    with HasSWOT
    with CanCompose
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
  def decision(key: Key): Option[Decision] = component(key, classOf[Decision])
  def decisionOption(key: Key): Option[DecisionOption] =
    component(key, classOf[DecisionOption])

  def functionalRequirementScores(
      decisionOption: DecisionOption
  ): List[(Requirement, RequirementScore)] =
    scores(decisionOption).filter(_._1.isInstanceOf[FunctionalRequirement])

  def qualityAttributeRequirementScores(
      decisionOption: DecisionOption
  ): List[(Requirement, RequirementScore)] = scores(decisionOption).filter(
    _._1.isInstanceOf[QualityAttributeRequirement]
  )

  def scores(
      decisionOption: DecisionOption
  ): List[(Requirement, RequirementScore)] =
    decision(decisionOption)
      .map(_.requirements.map(r => (r, decisionOption.score(r.key))))
      .getOrElse(Nil)

  def decision(decisionOption: DecisionOption): Option[Decision] =
    directIncomingDependencies(
      decisionOption,
      classOf[Composition],
      classOf[Decision]
    ).headOption

  def constraintScores(
      decisionOption: DecisionOption
  ): List[(Requirement, RequirementScore)] =
    scores(decisionOption).filter(_._1.isInstanceOf[Constraint])

  def optionsUnderInvestigation(decision: Decision): List[DecisionOption] =
    options(decision).filter(_.verdict.isInstanceOf[UnderInvestigation])

  def options(decision: Decision): List[DecisionOption] = {
    directOutgoingDependencies(
      decision,
      classOf[Composition],
      classOf[DecisionOption]
    ).map(o =>
      if (isDecisionOptionRejected(o)) o.copy(verdict = Rejected()) else o
    )
  }

  private def isDecisionOptionRejected(
      decisionOption: DecisionOption
  ): Boolean = scores(decisionOption).exists(rr =>
    rr._1.weight == MustHave && rr._2.isInstanceOf[DoesNotMeetExpectations]
  )

  def chosenOptions(decision: Decision): List[DecisionOption] =
    options(decision).filter(_.verdict.isInstanceOf[Chosen])

  def rejectedOptions(decision: Decision): List[DecisionOption] =
    options(decision).filter(_.verdict.isInstanceOf[Rejected])

  def enterprise(decision: Decision): Option[Enterprise] =
    directOutgoingDependencies(
      decision,
      classOf[Serving],
      classOf[Enterprise]
    ).headOption

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

case class UnderInvestigation(description: String = "")
    extends DecisionOptionVerdict {
  val name = "Under investigation"
}

case class Chosen(description: String = "") extends DecisionOptionVerdict {
  val name = "Chosen"
}

case class Rejected(description: String = "") extends DecisionOptionVerdict {
  val name = "Rejected"
}

case class DecisionConfigurer(
    modelComponent: Decision,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[Decision]
    with CanConfigureLinks[Decision]
    with CanConfigureDataProtectionConcerns[Decision]
    with CanConfigureSecurityImpact[Decision]
    with CanConfigureCompositionSource[Decision]
    with CanConfigureServingSource[Decision]
    with CanConfigureImpactSource[Decision]
    with CanConfigureFatherTime[Decision]
    with CanConfigureStakeHolderTarget[Decision]
    with CanConfigureRaciTarget[Decision]
    with CanConfigureInfluenceTarget[Decision]
    with CanConfigureTriggerTarget[Decision]
    with CanConfigureAssociations[Decision]
    with CanConfigureRequirements[Decision]
    with CanConfigureContext[Decision] {
  def as(
      body: DecisionConfigurer => Any
  ): Decision = {
    body.apply(this)
    propertyAdder.townPlan
      .decision(modelComponent.key)
      .get
  }
}

case class DecisionOptionConfigurer(
    modelComponent: DecisionOption,
    propertyAdder: CanAddRequirementScores,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[DecisionOption]
    with CanConfigureLinks[DecisionOption]
    with CanConfigureSWOT[DecisionOption]
    with CanConfigureDataProtectionConcerns[DecisionOption]
    with CanConfigureCompositionTarget[DecisionOption]
    with CanConfigureAssociations[DecisionOption]
    with CanConfigureRequirementScores[DecisionOption]
    with CanConfigureCosts[DecisionOption] {
  def as(
      body: DecisionOptionConfigurer => Any
  ): DecisionOption = {
    body.apply(this)
    propertyAdder.townPlan
      .decisionOption(modelComponent.key)
      .get
  }
}

trait CanAddDecisions
    extends CanAddProperties
    with CanAddRelationships
    with CanAddRequirementScores {
  def describes(decision: Decision): DecisionConfigurer =
    DecisionConfigurer(has(decision), this, this)
  def describes(decisionOption: DecisionOption): DecisionOptionConfigurer =
    DecisionOptionConfigurer(has(decisionOption), this, this)
}
