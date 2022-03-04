package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.{
  Description,
  GoneToProduction
}
import com.innovenso.townplanner.model.concepts.views.{
  CompiledIntegrationMap,
  CompiledSystemIntegrationView,
  IntegrationMap,
  SystemIntegrationView
}
import com.innovenso.townplanner.model.concepts.{
  EnterpriseArchitectureContext,
  ItSystem,
  ItSystemIntegration
}
import com.innovenso.townplanner.model.meta.{Day, InTheFuture, Today}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class IntegrationMapSpec extends AnyFlatSpec with GivenWhenThen {
  "an integration map" can "be added to the townplan" in new EnterpriseArchitectureContext {
    Given("some systems")
    val system1: ItSystem = ea has ItSystem(title = "System 1")
    val system2: ItSystem = ea has ItSystem(title = "System 2")
    val system3: ItSystem = ea has ItSystem(title = "System 3")
    val system4: ItSystem = ea has ItSystem(title = "System 4")

    And("some integrations")
    val integration1: ItSystemIntegration =
      ea describes ItSystemIntegration(title =
        "Integration 1"
      ) between system1 and system2 as { it =>
        it has Description("An Integration")
      }

    val integration2: ItSystemIntegration =
      ea describes ItSystemIntegration(title =
        "Integration 2"
      ) between system2 and system3 as { it =>
        it has Description("Another Integration")
        it is GoneToProduction() on Day(2023, 10, 1)
      }

    When("a system integration view is requested")
    val integrationMapToday: IntegrationMap =
      ea needs IntegrationMap(pointInTime = Today)

    val integrationMapFuture: IntegrationMap =
      ea needs IntegrationMap(pointInTime = InTheFuture)

    Then("the integration map exists")
    assert(exists(integrationMapToday))
    assert(exists(integrationMapFuture))

    val compiledViewToday: CompiledIntegrationMap =
      townPlan.integrationMap(integrationMapToday.key).get
    val compiledViewFuture: CompiledIntegrationMap =
      townPlan.integrationMap(integrationMapFuture.key).get

    And("it contains the right elements and relationships")
    assert(compiledViewToday.system(system1.key).contains(system1))
    assert(compiledViewToday.system(system2.key).contains(system2))
    assert(compiledViewToday.system(system3.key).isEmpty)
    assert(compiledViewToday.system(system4.key).isEmpty)
    assert(compiledViewToday.relationships.size == 1)

    assert(compiledViewFuture.system(system1.key).contains(system1))
    assert(compiledViewFuture.system(system2.key).contains(system2))
    assert(compiledViewFuture.system(system3.key).contains(system3))
    assert(compiledViewFuture.system(system4.key).isEmpty)
    assert(compiledViewFuture.relationships.size == 2)

  }
}
