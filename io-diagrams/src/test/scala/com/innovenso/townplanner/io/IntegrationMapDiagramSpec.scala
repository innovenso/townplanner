package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.properties.{
  Description,
  GoneToProduction
}
import com.innovenso.townplanner.model.concepts.views.IntegrationMap
import com.innovenso.townplanner.model.concepts.{ItSystem, ItSystemIntegration}
import com.innovenso.townplanner.model.meta.{Day, InTheFuture, Today}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import java.time.LocalDate

class IntegrationMapDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "an integration map" can "be added to the townplan" in new DiagramIO {
    Given("some systems")
    val system1: ItSystem = samples.system()
    val system2: ItSystem = samples.system()
    val system3: ItSystem = samples.system()
    val system4: ItSystem = samples.system()

    And("some integrations")
    val integration1: ItSystemIntegration =
      samples.integration(system1, system2)
    val integration2: ItSystemIntegration =
      samples.integration(
        system2,
        system3,
        Set(samples.goneToProduction(LocalDate.now().getYear + 1, 10, 1))
      )

    When("a system integration view is requested")
    val integrationMapToday: IntegrationMap =
      ea needs IntegrationMap(pointInTime = Today)
    val integrationMapFuture: IntegrationMap =
      ea needs IntegrationMap(pointInTime = InTheFuture)

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.integrationMap(integrationMapToday.key)
      )
    )
    assert(
      specificationExists(
        townPlan.integrationMap(integrationMapFuture.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(integrationMapToday.key))
    assert(diagramsAreWritten(integrationMapFuture.key))
  }
}
