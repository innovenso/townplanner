package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.properties.{
  Message,
  Request,
  Response
}
import com.innovenso.townplanner.model.concepts.views.FlowView
import com.innovenso.townplanner.model.concepts.{
  Actor,
  ItSystem,
  Microservice
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class FlowViewDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "a specification and diagram" should "be written for each flow view" in new DiagramIO {
    Given("some systems")
    val system1: ItSystem = ea has ItSystem(title = "A System")
    val system2: ItSystem = ea has ItSystem(title = "Another System")
    And("a user")
    val user: Actor = ea has Actor(title = "A user")
    And("a container")
    val container1: Microservice =
      ea describes Microservice(title = "A microservice") as { it =>
        it isPartOf system1
      }

    When("a flow view is requested")
    val flowView: FlowView = ea needs FlowView(title = "The Flow View") and {
      it =>
        it has Request("once ") from user to container1
        it has Request("upon ") from container1 to system2
        it has Response("a ") from system2 to container1
        it has Message("midnight ") from container1 to system1
        it has Response("dreary") from container1 to user
    }

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.flowView(flowView.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(flowView.key))

  }
}
