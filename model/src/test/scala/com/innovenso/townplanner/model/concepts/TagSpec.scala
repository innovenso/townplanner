package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class TagSpec extends AnyFlatSpec with GivenWhenThen {
  "Tags" can "be added to the town plan" in new EnterpriseArchitectureContext {
    When("a tag is added to the town plan")
    val theTag: Tag =
      ea describes Tag(title = "The Tag") as { it =>
        it has Description("a description")
      }

    Then("the tag exists")
    assert(exists(theTag))
    println(theTag.color)
  }
}
