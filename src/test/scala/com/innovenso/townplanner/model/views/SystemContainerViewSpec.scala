package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.GoneToProduction
import com.innovenso.townplanner.model.concepts.views.{
  CompiledSystemContainerView,
  SystemContainerView
}
import com.innovenso.townplanner.model.concepts.{
  ActorNoun,
  Database,
  EnterpriseArchitecture,
  IndividualActor,
  ItSystem,
  Microservice
}
import com.innovenso.townplanner.model.meta.{Day, InTheFuture}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class SystemContainerViewSpec extends AnyFlatSpec with GivenWhenThen {
  "System container views" can "be added to the town plan" in new EnterpriseArchitecture {
    val system1: ItSystem = ea has ItSystem(title = "System 1")
    val system2: ItSystem = ea has ItSystem(title = "System 2")
    val system3: ItSystem = ea has ItSystem(title = "System 3")
    val system4: ItSystem = ea describes ItSystem(title = "System 4") as { it =>
      it has GoneToProduction() on InTheFuture
    }

    val ms1: Microservice =
      ea describes Microservice(title = "Microservice 1") as { it =>
        it isPartOf system1
      }

    val db1: Database = ea describes Database(title = "Database 1") as { it =>
      it isPartOf system1
      it isUsedBy ms1
    }

    val ms2: Microservice =
      ea describes Microservice(title = "Microservice 2") as { it =>
        it isPartOf system2
        it isUsedBy ms1
        it isUsedBy system1
        it uses system3
        it uses system4
      }

    val user1: ActorNoun = ea describes ActorNoun(title = "User 1") as { it =>
      it uses ms2
      it uses system1
      it uses system4
    }

    val user2: ActorNoun = ea has ActorNoun(title = "User 2")

    val jurgenlust: IndividualActor =
      ea describes IndividualActor(title = "Jurgen Lust") as { he =>
        he delivers system1
        he delivers ms1
        he delivers db1
      }

    val system2ContainerView: SystemContainerView =
      ea needs SystemContainerView(forSystem = system2.key)

    println(system2ContainerView)

    assert(exists(system2ContainerView))
    val compiledSystemContainerView: CompiledSystemContainerView = townPlan
      .systemContainerView(system2ContainerView.key)
      .get
    println(compiledSystemContainerView)
    assert(
      compiledSystemContainerView.systems.size == 3
    )
    assert(
      compiledSystemContainerView.system(system1.key).contains(system1)
    )
    assert(
      compiledSystemContainerView.system(system2.key).contains(system2)
    )
    assert(
      compiledSystemContainerView.system(system3.key).contains(system3)
    )
    assert(
      compiledSystemContainerView.system(system4.key).isEmpty
    )
    assert(
      compiledSystemContainerView.container(ms2.key).contains(ms2)
    )
    assert(
      compiledSystemContainerView.container(ms1.key).isEmpty
    )
    assert(
      compiledSystemContainerView.container(db1.key).isEmpty
    )
    assert(
      compiledSystemContainerView.businessActor(user1.key).contains(user1)
    )
    assert(
      compiledSystemContainerView.businessActor(user2.key).isEmpty
    )
    assert(
      compiledSystemContainerView.businessActor(jurgenlust.key).isEmpty
    )

  }
}
