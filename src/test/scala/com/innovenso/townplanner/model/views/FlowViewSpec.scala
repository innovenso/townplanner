package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.{
  Message,
  Request,
  Response
}
import com.innovenso.townplanner.model.concepts.views.FlowView
import com.innovenso.townplanner.model.concepts.{
  ActorNoun,
  EnterpriseArchitecture,
  ItSystem,
  Microservice
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class FlowViewSpec extends AnyFlatSpec with GivenWhenThen {
  "A flow view" can "be added to the town plan" in new EnterpriseArchitecture {
    val system1: ItSystem = ea has ItSystem(title = "A System")
    val system2: ItSystem = ea has ItSystem(title = "Another System")
    val user: ActorNoun = ea has ActorNoun(title = "A user")
    val container1: Microservice =
      ea describes Microservice(title = "A microservice") as { it =>
        it isPartOf system1
      }

    val flowView: FlowView = ea describes FlowView(title = "The Flow View") as {
      it =>
        it has Request("once ") from user to container1
        it has Request("upon ") from container1 to system2
        it has Response("a ") from system2 to container1
        it has Message("midnight ") from container1 to system1
        it has Response("dreary") from container1 to user
    }

    assert(exists(flowView))
    assert(
      townPlan
        .flowView(flowView.key)
        .exists(it =>
          it.interactions
            .map(_.name)
            .mkString == "once upon a midnight dreary"
        )
    )
  }
}
