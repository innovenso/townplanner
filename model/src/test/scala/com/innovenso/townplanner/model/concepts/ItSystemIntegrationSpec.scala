package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.concepts.relationships.Implementation
import com.innovenso.townplanner.model.concepts.views.FlowView
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItSystemIntegrationSpec extends AnyFlatSpec with GivenWhenThen {
  "IT System Integrations" can "be added to the townplan" in new EnterpriseArchitectureContext {
    Given("some systems")
    val system1: ItSystem = ea has ItSystem(title = "System 1")
    val system2: ItSystem = ea has ItSystem(title = "System 2")

    val flowView1: FlowView = ea needs FlowView(title = "Flow View 1") and {
      it =>
        it has Request("do something") from system1 to system2
        it has Response("response") from system2 to system1
    }

    And("an integration platform")
    val integrationPlatform: ItSystem =
      ea has ItSystem(title = "Integration Platform")
    When("an integration is added to the town plan")
    val integration: ItSystemIntegration =
      ea describes ItSystemIntegration(title =
        "An Integration"
      ) between system1 and system2 as { it =>
        it has Description("This is an integration")
        it should BeInvestedIn()
        it ratesFailureAs Catastrophic(consequences = "people die")
        it provides ResilienceMeasure("circuit breaker")
        it has Volume("thousands per day")
        it has Frequency("every second")
        it isImplementedBy integrationPlatform
        it isIllustratedBy FlowViewIllustration(flowView = flowView1)

        it has Message("step 1") from system1 to integrationPlatform
        it has Request("step 2") from integrationPlatform to system2
        it has Response("step 3") from system2 to integrationPlatform
        it has Message("step 4") from integrationPlatform to system1
      }

    Then("the integration exists")
    assert(exists(integration))
    And("it has a throughput")
    assert(
      townPlan
        .systemIntegration(integration.key)
        .exists(it => it.frequency.nonEmpty && it.volume.nonEmpty)
    )
    And("it has the correct relationships with the integration platform")
    assert(
      townPlan
        .relationships(
          integrationPlatform,
          classOf[Implementation],
          classOf[ItSystemIntegration]
        )
        .size == 1
    )
    And("it is linked to the correct systems")
    assert(townPlan.systemIntegrations(system1, system2).size == 1)
    And("it has the correct sequence of interactions")
    assert(
      townPlan
        .systemIntegration(integration.key)
        .exists(it => it.interactions.size == 4)
    )
    assert(
      townPlan
        .systemIntegration(integration.key)
        .exists(it =>
          it.interactions.map(_.name).map(_.takeRight(1)).mkString == "1234"
        )
    )
    And("it has the correct flow view illustrations")
    assert(
      townPlan
        .systemIntegration(integration.key)
        .exists(it =>
          it.flowViewIllustrations.head.flowViewKey == flowView1.key
        )
    )
  }
}
