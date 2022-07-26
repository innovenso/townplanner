package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.concepts.properties.GoneToProduction
import com.innovenso.townplanner.model.concepts.views.{
  CompiledPlatformSystemView,
  CompiledSystemContainerView,
  PlatformSystemView,
  SystemContainerView
}
import com.innovenso.townplanner.model.meta.{InTheFuture, Key}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class PlatformSystemViewSpec extends AnyFlatSpec with GivenWhenThen {
  "Platform system views" can "be added to the town plan" in new EnterpriseArchitectureContext {
    Given("a platform")
    val platform = samples.platform()
    val platform2 = samples.platform()
    Given("some systems")
    val system1: ItSystem =
      ea describes ItSystem(key = Key("system1"), title = "System 1") as { it =>
        it isPartOf platform
      }
    val system2: ItSystem =
      ea describes ItSystem(key = Key("system2"), title = "System 2") as { it =>
        it isPartOf platform
      }
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

    val ms3: Microservice =
      ea describes Microservice(key = Key("ms3"), title = "Microservice 3") as {
        it =>
          it isPartOf system3
          it isUsedBy ms1
          it isUsedBy system1
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

    When("a platform system view is requested")
    val platformSystemView: PlatformSystemView =
      ea needs PlatformSystemView(forPlatform = platform, withContainers = true)

    Then("the system container view exists")
    assert(exists(platformSystemView))
    val compiledPlatformSystemView: CompiledPlatformSystemView = townPlan
      .platformSystemView(platformSystemView.key)
      .get
    And("it only contains the relevant platforms")
    assert(compiledPlatformSystemView.platforms.size == 1)
    And("it only contains the relevant systems")
    assert(
      compiledPlatformSystemView.systems.size == 3
    )
    assert(
      compiledPlatformSystemView.system(system1.key).contains(system1)
    )
    assert(
      compiledPlatformSystemView.system(system2.key).contains(system2)
    )
    assert(
      compiledPlatformSystemView.system(system3.key).contains(system3)
    )
    assert(
      compiledPlatformSystemView.system(system4.key).isEmpty
    )
    And("it only contains the relevant containers")
    assert(
      compiledPlatformSystemView.container(ms2.key).contains(ms2)
    )
    assert(
      compiledPlatformSystemView.container(ms1.key).contains(ms1)
    )
    assert(
      compiledPlatformSystemView.container(db1.key).contains(db1)
    )
    assert(
      compiledPlatformSystemView.container(ms3.key).isEmpty
    )
    And("it only contains the relevant actors")
    assert(
      compiledPlatformSystemView.businessActor(user1.key).contains(user1)
    )
    assert(
      compiledPlatformSystemView.businessActor(user2.key).isEmpty
    )
    assert(
      compiledPlatformSystemView.businessActor(jurgenlust.key).isEmpty
    )

  }
}
