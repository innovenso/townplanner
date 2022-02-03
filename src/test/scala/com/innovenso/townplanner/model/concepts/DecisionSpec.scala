package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.properties.{
  DoesNotMeetExpectations,
  ExceedsExpectations,
  FunctionalRequirement,
  MustHave
}
import com.innovenso.townplanner.model.meta.{Description, Key, Title}
import com.innovenso.townplanner.model.test.Factory
import org.scalatest.{Assertion, GivenWhenThen}
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Try

class DecisionSpec extends AnyFlatSpec with GivenWhenThen {
  "Decisions" should "be addable to the town plan" in new Factory {
    val result =
      factory
        .withDecision(
          title = Title("Cloud Strategy"),
          status = NotStarted,
          outcome = Description(None)
        )
    assert(result.isSuccess)
    assert(result.get._1.has(result.get._2))
  }

  "Decisions" should "have requirements" in new Factory {
    val result: Try[(TownPlan, DecisionOption)] =
      factory
        .withDecision(
          key = Key("cloudstrategy"),
          title = Title("Cloud Strategy"),
          status = NotStarted,
          outcome = Description(None)
        )
        .flatMap(td =>
          factory.withRequirement(
            td._2,
            FunctionalRequirement(
              key = Key("funcreq1"),
              title = Title("func req"),
              weight = MustHave,
              description = Description(Some(""))
            )
          )
        )
        .flatMap(td =>
          factory.withOption(
            decision = td._2,
            title = Title("AWS native"),
            verdict = UnderInvestigation
          )
        )
        .flatMap(tdd =>
          factory.withRequirementScore(
            modelComponent = tdd._3,
            requirementsHolderKey = tdd._2.key,
            requirementKey = Key("funcreq1"),
            weight = DoesNotMeetExpectations
          )
        )
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val option: DecisionOption = result.get._2
    assert(townPlan.has(option))
    assert(option.decisionKey == Key("cloudstrategy"))
    assert(option.verdict == UnderInvestigation)
    assert(townPlan.scores(option).size == 1)
    val decision: Decision = townPlan.decisions.head
    assert(townPlan.options(decision).head.verdict == Rejected)
    assert(townPlan.rejectedOptions(decision).size == 1)
    assert(townPlan.optionsUnderInvestigation(decision).isEmpty)
    assert(townPlan.chosenOptions(decision).isEmpty)
  }

}
