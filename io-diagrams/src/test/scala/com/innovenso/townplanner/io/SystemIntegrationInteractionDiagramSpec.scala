package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.concepts.properties.{
  Message,
  Request,
  Response
}
import com.innovenso.townplanner.model.concepts.views.{
  CompiledSystemIntegrationInteractionView,
  SystemIntegrationInteractionView
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class SystemIntegrationInteractionDiagramSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "A system integration interaction view" can "be added to the town plan" in new DiagramIO {
    Given("some systems")
    val system1: ItSystem = ea has ItSystem(title = "A System")
    val system2: ItSystem = ea has ItSystem(title = "Another System")
    And("a user")
    val user: Actor = ea has Actor(title = "A user")
    And("some containers")
    val microservice: Microservice =
      ea describes Microservice(title = "A microservice") as { it =>
        it isPartOf system1
      }

    val database: Database = ea describes Database(title = "A database") as {
      it =>
        it isPartOf system1
    }
    And("a system integration")

    val integration: ItSystemIntegration =
      ea describes ItSystemIntegration(title =
        "the integration"
      ) between system1 and system2 as { it =>
        it has Request("once ") from user to microservice
        it has Request("upon ") from microservice to database
        it has Response("a ") from database to microservice
        it has Message("midnight ") from microservice to system2
        it has Response("dreary") from microservice to user
      }

    When("a system integration interaction view is requested")
    val viewUnderTest: SystemIntegrationInteractionView =
      ea needs SystemIntegrationInteractionView(
        title = "The Interaction View",
        forSystemIntegration = integration.key,
        withStepCounter = false
      )

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.systemIntegrationInteractionView(viewUnderTest.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(viewUnderTest.key))

  }
}
