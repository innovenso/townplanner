package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  BeInvestedIn,
  Catastrophic,
  Description,
  Frequency,
  Message,
  Request,
  ResilienceMeasure,
  Response,
  Volume
}
import com.innovenso.townplanner.model.concepts.relationships.Implementation
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItSystemIntegrationSpec extends AnyFlatSpec with GivenWhenThen {
  "IT System Integrations" can "be added to the townplan" in new EnterpriseArchitecture {
    val system1: ItSystem = ea has ItSystem(title = "System 1")
    val system2: ItSystem = ea has ItSystem(title = "System 2")
    val integrationPlatform: ItSystem =
      ea has ItSystem(title = "Integration Platform")
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

        it has Message("step 1") from system1 to integrationPlatform
        it has Request("step 2") from integrationPlatform to system2
        it has Response("step 3") from system2 to integrationPlatform
        it has Message("step 4") from integrationPlatform to system1
      }

    assert(exists(integration))
    assert(
      townPlan
        .systemIntegration(integration.key)
        .exists(it => it.frequency.nonEmpty && it.volume.nonEmpty)
    )
    assert(
      townPlan
        .relationships(
          integrationPlatform,
          classOf[Implementation],
          classOf[ItSystemIntegration]
        )
        .size == 1
    )
    assert(townPlan.systemIntegrations(system1, system2).size == 1)
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
  }
}
