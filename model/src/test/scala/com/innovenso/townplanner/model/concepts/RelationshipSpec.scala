package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.relationships.{Flow, RelationshipConfigurer}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class RelationshipSpec extends AnyFlatSpec with GivenWhenThen {
  "relationships" can "be configured with description and father time" in new EnterpriseArchitectureContext {
    Given("some systems")
    val system1: ItSystem = ea has ItSystem(title = "system 1")
    val system2: ItSystem = samples.system()
    When("a system has a relationship with other systems")
    val system3: ItSystem = ea describes ItSystem(title = "a third system") as { it =>
      it isUsing system1 and { that => that has Description("Hello world") }
      it isBeingUsedBy system2 and { that => that has Description("Hello again") }
    }
    Then("the relationship has a description")
    assert(townPlan.relationships.exists(r => r.descriptions.exists(d => d.value == "Hello world")))
    assert(townPlan.relationships.exists(r => r.descriptions.exists(d => d.value == "Hello again")))
    println(townPlan.relationships(system3, classOf[Flow]).head)
  }
}
