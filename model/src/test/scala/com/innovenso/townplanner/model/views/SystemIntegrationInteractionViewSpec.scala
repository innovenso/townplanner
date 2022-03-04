package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.{
  Message,
  Request,
  Response
}
import com.innovenso.townplanner.model.concepts.views.{
  CompiledSystemIntegrationInteractionView,
  SystemIntegrationInteractionView
}
import com.innovenso.townplanner.model.concepts._
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class SystemIntegrationInteractionViewSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "A system integration interaction view" can "be added to the town plan" in new EnterpriseArchitectureContext {
    Given("some systems")
    val system1: ItSystem = ea has ItSystem(title = "A System")
    val system2: ItSystem = ea has ItSystem(title = "Another System")
    And("a user")
    val user: ActorNoun = ea has ActorNoun(title = "A user")
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
    ea needs SystemIntegrationInteractionView(
      title = "The Interaction View",
      forSystemIntegration = integration.key
    )

    Then("it should exist")
    val compiledView: Option[CompiledSystemIntegrationInteractionView] =
      townPlan.systemIntegrationInteractionViews.headOption

    println(compiledView)

    assert(
      compiledView.exists(v =>
        v.containers.contains(microservice) && v.containers.contains(database)
      )
    )
    assert(
      compiledView.exists(_.systemContexts.contains(system1))
    )
    assert(
      compiledView.exists(_.otherSystems.contains(system2))
    )
    assert(compiledView.exists(_.actorNouns.contains(user)))

    And("the steps should be ordered")
    assert(
      compiledView
        .exists(it =>
          it.interactions
            .map(_.name)
            .mkString == "once upon a midnight dreary"
        )
    )
  }
}
