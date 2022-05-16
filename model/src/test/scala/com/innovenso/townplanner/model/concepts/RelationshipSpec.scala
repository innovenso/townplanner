package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.relationships.{
  Flow,
  RelationshipConfigurer
}
import com.innovenso.townplanner.model.meta.Key
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import scala.language.postfixOps

class RelationshipSpec extends AnyFlatSpec with GivenWhenThen {
  "relationships" can "be configured with description and father time" in new EnterpriseArchitectureContext {
    Given("some systems")
    val system1: ItSystem = ea has ItSystem(title = "system 1")
    val system2: ItSystem = samples.system()
    When("a system has a relationship with other systems")
    val system3: ItSystem = ea describes ItSystem(title = "a third system") as {
      it =>
        it isUsing system1 and { that => that has Description("Hello world") }
        it isBeingUsedBy system2 and { that =>
          that has Description("Hello again")
        }
    }
    Then("the relationship has a description")
    assert(
      townPlan.relationships.exists(r =>
        r.descriptions.exists(d => d.value == "Hello world")
      )
    )
    assert(
      townPlan.relationships.exists(r =>
        r.descriptions.exists(d => d.value == "Hello again")
      )
    )
    println(townPlan.relationships(system3, classOf[Flow]).head)
  }

  "flows" can "be configured in a fluent way" in new EnterpriseArchitectureContext {
    Given("some systems")
    val system1: ItSystem =
      ea has ItSystem(key = Key("system1"), title = "system 1")
    val system2: ItSystem =
      ea has ItSystem(key = Key("system2"), title = "system 2")
    When("a new system gets relationships described")
    val system3: ItSystem =
      ea describes ItSystem(key = Key("system3"), title = "System 3") as { it =>
        it does "sends messages" to system1 period

        it does "receive messages" on system2 and { that =>
          that has Description("nice isn't it?")
        }

        it isDone "requests status" by system1 and { that =>
          that has Description("synchronously")
        }
      }
    Then("the relationships exist")
    townPlan.relationships.foreach(println(_))
    assert(
      townPlan.relationships.exists(r =>
        r.source == system3.key && r.target == system1.key && r.title == "sends messages"
      )
    )
    assert(
      townPlan.relationships.exists(r =>
        r.source == system3.key && r.target == system2.key && r.title == "receive messages"
      )
    )
    assert(
      townPlan.relationships.exists(r =>
        r.source == system1.key && r.target == system3.key && r.title == "requests status"
      )
    )
  }
}
