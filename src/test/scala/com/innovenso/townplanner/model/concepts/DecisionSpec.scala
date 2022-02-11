package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  Assumption,
  Constraint,
  CurrentState,
  Description,
  DoesNotMeetExpectations,
  ExceedsExpectations,
  FunctionalRequirement,
  Goal,
  MeetsExpectations,
  MustHave,
  QualityAttributeRequirement,
  ShouldHave
}
import com.innovenso.townplanner.model.meta.Key
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class DecisionSpec extends AnyFlatSpec with GivenWhenThen {
  "Decisions" can "be added to the town plan" in new EnterpriseArchitecture {
    val innovenso: Enterprise = ea has Enterprise(title = "Innovenso")
    val jurgen: IndividualActor = ea has IndividualActor(title = "Jurgen Lust")
    val virginie: IndividualActor =
      ea has IndividualActor(title = "Virginie Héloire")
    val paymentsCapability: BusinessCapability =
      ea describes BusinessCapability(title = "Handle Payments") as { it =>
        it serves innovenso
      }
    val psp: ArchitectureBuildingBlock =
      ea describes ArchitectureBuildingBlock(title = "PSP") as { it =>
        it has Description("Payment Service Provider")
        it realizes paymentsCapability
      }
    val paypal: ItSystem = ea describes ItSystem(title = "PayPal") as { it =>
      it realizes psp
    }

    val pspSelection: Decision =
      ea describes Decision(title = "PSP Vendor Selection") as { it =>
        it has Description(
          "one vendor should be chosen to handle all our payments"
        )
        it has CurrentState(description =
          "1. we have direct integrations with a number of payment methods"
        )
        it has CurrentState(description =
          "2. we spend a lot of money on maintaining the integrations and keeping them secure"
        )
        it has Goal(description = "to have one single PSP to integrate")
        it has Goal(description =
          "to be able to support all possible payment methods"
        )
        it has Assumption(description =
          "We are only looking for a solution for the online platform, not for the physical stores"
        )
        it serves innovenso
        it has FunctionalRequirement(
          key = Key("psp_wip"),
          title = "Support Vendor-Initiated Payments",
          weight = ShouldHave
        )
        it has FunctionalRequirement(
          key = Key("acquiring"),
          title = "Have a acquiring license",
          weight = MustHave
        )
        it has FunctionalRequirement(title = "Something else")
        it has QualityAttributeRequirement(
          title = "Availability",
          sourceOfStimulus = "a user",
          stimulus = "tries to make a payment",
          response = "the PSP handles the payment",
          responseMeasure = "99.99% of the time"
        )
        it has Constraint(title = "We only work with European companies")
        it has Constraint(title = "The budget of course")
        it changes paymentsCapability
        it keeps psp
        it removes paypal
        it hasStakeholder jurgen
        it isResponsibilityOf virginie
        it hasInformed jurgen
        it hasConsulted jurgen
      }

    val adyen: DecisionOption =
      ea describes DecisionOption(title = "AdYen") as { it =>
        it isPartOf pspSelection
        it scores ExceedsExpectations(description =
          "very nice"
        ) on (pspSelection, Key("psp_wip"))
        it scores DoesNotMeetExpectations() on (pspSelection, Key("acquiring"))
      }

    assert(exists(pspSelection))
    assert(
      townPlan.decision(pspSelection.key).get.functionalRequirements.size == 3
    )
    assert(townPlan.decision(pspSelection.key).get.constraints.size == 2)
    assert(
      townPlan
        .decision(pspSelection.key)
        .get
        .qualityAttributeRequirements
        .size == 1
    )
    assert(
      townPlan
        .decision(pspSelection.key)
        .get
        .currentState
        .head
        .description
        .startsWith("1.")
    )

    assert(townPlan.options(pspSelection).size == 1)
    townPlan.functionalRequirementScores(adyen).foreach(println(_))
    assert(townPlan.functionalRequirementScores(adyen).size == 3)
    assert(townPlan.qualityAttributeRequirementScores(adyen).size == 1)
    assert(townPlan.constraintScores(adyen).size == 2)
    assert(townPlan.rejectedOptions(pspSelection).size == 1)
    assert(townPlan.chosenOptions(pspSelection).isEmpty)
    assert(townPlan.optionsUnderInvestigation(pspSelection).isEmpty)
  }
}
