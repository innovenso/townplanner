package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Description
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class PrincipleSpec extends AnyFlatSpec with GivenWhenThen {
  "principles" can "be added to the town plan" in new EnterpriseArchitecture {
    val innovenso: Enterprise = ea has Enterprise(title = "Innovenso")
    val doItRight: CorporatePrinciple = ea describes CorporatePrinciple(title =
      "We Do It Right or We Don't Do It"
    ) as { it =>
      it has Description("It's about focus")
      it serves innovenso
    }

    assert(exists(doItRight))
    assert(townPlan.relationships.size == 1)
  }

}
