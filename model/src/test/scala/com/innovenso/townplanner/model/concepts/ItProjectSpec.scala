package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.meta.{Day, Key, Today}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItProjectSpec extends AnyFlatSpec with GivenWhenThen {
  "IT Projects" can "be added to the town plan" in new EnterpriseArchitectureContext {
    Given("an enterprise")
    val innovenso: Enterprise = ea has Enterprise(title = "Innovenso")
    And("some individuals")
    val jurgen: Person = ea has Person(title = "Jurgen Lust")
    val virginie: Person =
      ea has Person(title = "Virginie Héloire")
    And("some capabilities")

    val paymentsCapability: BusinessCapability =
      ea describes BusinessCapability(title = "Handle Payments") as { it =>
        it serves innovenso
      }
    And("some architecture building blocks")

    val psp: ArchitectureBuildingBlock =
      ea describes ArchitectureBuildingBlock(title = "PSP") as { it =>
        it has Description("Payment Service Provider")
        it realizes paymentsCapability
      }
    And("a system")
    val paypal: ItSystem = ea describes ItSystem(title = "PayPal") as { it =>
      it realizes psp
    }

    When("a project is added to the town plan")
    val pspImplementationProject: ItProject =
      ea describes ItProject(title = "PSP Implementation") as { it =>
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
        it dealsWith PCICompliance(
          "no payment data should be transported over our network, or stored on our servers"
        )
        it has ExtremelyHighImpact on Confidentiality(description =
          "payment data!"
        )
        it is Due() on Day(2022, 8, 1)
      }

    And("milestones are added to the project")
    val adyenImplementation: ItProjectMilestone =
      ea describes ItProjectMilestone(title = "AdYen") as { it =>
        it isPartOf pspImplementationProject
        it is Due() on Today
      }

    Then("the project exists")
    assert(exists(pspImplementationProject))
    And("it has requirements")
    assert(
      townPlan
        .itProject(pspImplementationProject.key)
        .get
        .functionalRequirements
        .size == 3
    )
    And("it has constraints")
    assert(
      townPlan.itProject(pspImplementationProject.key).get.constraints.size == 2
    )
    And("it has quality attribute requirements")
    assert(
      townPlan
        .itProject(pspImplementationProject.key)
        .get
        .qualityAttributeRequirements
        .size == 1
    )
    And("it has the correct due date")

    assert(
      townPlan
        .itProject(pspImplementationProject.key)
        .get
        .dueDate
        .exists(_.date == Day(2022, 8, 1))
    )

    And("it has the correct number of milestones")
    assert(townPlan.itProjectMilestones(pspImplementationProject).size == 1)

    And("it has the right compliance concerns")
    assert(
      townPlan
        .itProject(pspImplementationProject.key)
        .exists(_.pciComplianceConcerns.nonEmpty)
    )
    And("and the right confidentiality impacts")
    assert(
      townPlan
        .itProject(pspImplementationProject.key)
        .exists(_.confidentialityImpact.nonEmpty)
    )
  }
}
