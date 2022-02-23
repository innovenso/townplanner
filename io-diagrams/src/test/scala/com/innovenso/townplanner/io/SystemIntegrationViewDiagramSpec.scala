package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.SystemIntegrationView
import com.innovenso.townplanner.model.concepts.{ItSystem, ItSystemIntegration}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class SystemIntegrationViewDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "a diagram specification and diagram" should "be written for each system integration view" in new DiagramIO {
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

    And("a diagram specification is written for it")
    assert(
      specificationExists(townPlan.systemIntegrationView(integrationView.key))
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(integrationView.key))

  }
}
