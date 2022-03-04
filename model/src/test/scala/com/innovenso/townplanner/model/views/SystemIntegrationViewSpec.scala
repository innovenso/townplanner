package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{
  CompiledSystemIntegrationView,
  SystemIntegrationView
}
import com.innovenso.townplanner.model.concepts.{
  EnterpriseArchitectureContext,
  ItSystem,
  ItSystemIntegration
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class SystemIntegrationViewSpec extends AnyFlatSpec with GivenWhenThen {
  "a system integration view" can "be added to the townplan" in new EnterpriseArchitectureContext {
    Given("some systems")
    val system1: ItSystem = ea has ItSystem(title = "System 1")
    val system2: ItSystem = ea has ItSystem(title = "System 2")
    val system3: ItSystem = ea has ItSystem(title = "System 3")

    And("an integration")
    val integration: ItSystemIntegration =
      ea describes ItSystemIntegration(title =
        "Integration 1"
      ) between system1 and system2 as { it =>
        it has Description("An Integration")
      }

    And("integration platforms")
    val implementer1: ItSystem =
      ea describes ItSystem(title = "Implementer 1") as { it =>
        it implements integration
      }

    val implementer2: ItSystem =
      ea describes ItSystem(title = "Implementer 2") as { it =>
        it implements integration
      }

    When("a system integration view is requested")
    val integrationView: SystemIntegrationView =
      ea needs SystemIntegrationView(forSystemIntegration = integration.key)

    Then("the integration view exists")
    assert(exists(integrationView))
    val compiledView: CompiledSystemIntegrationView =
      townPlan.systemIntegrationView(integrationView.key).get

    And("it contains the right elements")
    assert(compiledView.system(system1.key).contains(system1))
    assert(compiledView.system(system2.key).contains(system2))
    assert(compiledView.system(system3.key).isEmpty)
    assert(
      compiledView.systemIntegration(integration.key).contains(integration)
    )
    And("it contains the right relationships")
    assert(compiledView.relationships.size == 4)
    And("it contains the right integration platforms")
    assert(compiledView.system(implementer1.key).contains(implementer1))
    assert(compiledView.system(implementer2.key).contains(implementer2))

  }
}
