package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.concepts.properties.GoneToProduction
import com.innovenso.townplanner.model.concepts.views.{
  CompiledSystemContainerView,
  SystemContainerView
}
import com.innovenso.townplanner.model.meta.{InTheFuture, Key}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class SystemContainerViewSpec extends AnyFlatSpec with GivenWhenThen {
  "System container views" can "be added to the town plan" in new EnterpriseArchitectureContext {
    Given("some systems")
    val system1: ItSystem =
      ea has ItSystem(key = Key("system1"), title = "System 1")
    val system2: ItSystem =
      ea has ItSystem(key = Key("system2"), title = "System 2")
    val system3: ItSystem =
      ea has ItSystem(key = Key("system3"), title = "System 3")
    And("a system that goes live in the future")
    val system4: ItSystem =
      ea describes ItSystem(key = Key("system4"), title = "System 4") as { it =>
        it has GoneToProduction() on InTheFuture
      }

    And("containers as part of the systems")
    val ms1: Microservice =
      ea describes Microservice(key = Key("ms1"), title = "Microservice 1") as {
        it =>
          it isPartOf system1
      }

    val db1: Database =
      ea describes Database(key = Key("db1"), title = "Database 1") as { it =>
        it isPartOf system1
        it isUsedBy ms1
      }

    val ms2: Microservice =
      ea describes Microservice(key = Key("ms2"), title = "Microservice 2") as {
        it =>
          it isPartOf system2
          it isUsedBy ms1
          it isUsedBy system1
          it uses system3
          it uses system4
      }

    And("some business actors")
    val user1: Actor =
      ea describes Actor(key = Key("user1"), title = "User 1") as { it =>
        it uses ms2
        it uses system1
        it uses system4
      }

    val user2: Actor =
      ea has Actor(key = Key("user2"), title = "User 2")

    And("some individuals")
    val jurgenlust: Person =
      ea describes Person(
        key = Key("jurgenlust"),
        title = "Jurgen Lust"
      ) as { he =>
        he delivers system1
        he delivers ms1
        he delivers db1
      }

    When("a system container view is requested")
    val system2ContainerView: SystemContainerView =
      ea needs SystemContainerView(forSystem = system2.key)

    println(system2ContainerView)

    Then("the system container view exists")
    assert(exists(system2ContainerView))
    val compiledSystemContainerView: CompiledSystemContainerView = townPlan
      .systemContainerView(system2ContainerView.key)
      .get
    println(compiledSystemContainerView)
    And("it only contains the relevant systems")
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
    And("it only contains the relevant containers")
    assert(
      compiledSystemContainerView.container(ms2.key).contains(ms2)
    )
    assert(
      compiledSystemContainerView.container(ms1.key).isEmpty
    )
    assert(
      compiledSystemContainerView.container(db1.key).isEmpty
    )
    And("it only contains the relevant actors")
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
