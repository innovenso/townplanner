package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.concepts.views.{
  PlatformSystemView,
  SystemContainerView
}
import com.innovenso.townplanner.model.meta.Key
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class PlatformSystemViewDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "A diagram specification and diagram" should "be written for each platform system view" in new DiagramIO {
    Given("some systems")
    val language: Language = samples.language
    val framework: Framework = samples.framework
    val platform1: ItPlatform = samples.platform()
    val platform2: ItPlatform = samples.platform()
    val system1: ItSystem = samples.system(
      withContainers = false,
      containingPlatform = Some(platform1)
    )
    val system2: ItSystem = samples.system(
      withContainers = false,
      containingPlatform = Some(platform1)
    )
    val system3: ItSystem = samples.system(withContainers = false)
    And("a system that goes live in the future")
    val system4: ItSystem =
      samples.system(fatherTime = Set(samples.goneToProduction(2500, 1, 1)))

    And("containers as part of the systems")
    val ms1: Microservice = samples.microservice(system1)

    val db1: Database = samples.database(system1)

    samples.flow(ms1, db1)

    samples.flow(system1, system2)
    samples.flow(system2, system3)

    val ms2: Microservice = samples.microservice(system2)

    val ms3: Microservice =
      ea describes Microservice(title = "Microservice 3") as { it =>
        it isPartOf system2
        it does "sends messages" to ms2 and { that =>
          that isImplementedBy language
          that isImplementedBy framework
        }
      }

    val db2: Database = samples.database(system2)
    samples.flow(ms3, db2)
    val queue1: Queue = samples.queue(system2)
    samples.flow(ms3, queue1)
    val ui2: WebUI = samples.ui(system2)
    samples.flow(ui2, ms3)

    samples.flow(ms1, ms2)
    samples.flow(system1, ms2)
    samples.flow(ms2, system3)
    samples.flow(ms2, system4)

    And("some business actors")
    val user1: Actor = samples.actor

    samples.flow(user1, ms2)
    samples.flow(user1, system1)
    samples.flow(user1, system4)

    val user2: Actor = samples.actor

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
    val platformSystemView: PlatformSystemView =
      ea needs PlatformSystemView(forPlatform = platform1)

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.platformSystemView(platformSystemView.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(platformSystemView.key))
  }
}
