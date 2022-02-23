package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.properties.{
  Description,
  GoneToProduction
}
import com.innovenso.townplanner.model.concepts.views.IntegrationMap
import com.innovenso.townplanner.model.concepts.{ItSystem, ItSystemIntegration}
import com.innovenso.townplanner.model.meta.{Day, Today}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class IntegrationMapDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "an integration map" can "be added to the townplan" in new DiagramIO {
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

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.integrationMap(integrationMapToday.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(integrationMapToday.key))
  }
}
