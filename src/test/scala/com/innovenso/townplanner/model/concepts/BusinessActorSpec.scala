package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Description
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class BusinessActorSpec extends AnyFlatSpec with GivenWhenThen {
  "Business Actors" can "be added to the town plan" in new EnterpriseArchitecture {
    val jurgen: IndividualActor =
      ea describes IndividualActor(title = "Jurgen Lust") as { he =>
        he has Description("Co-Founder of Innovenso")
        he has Description("Author of the Townplanner")
        he has Description("CTO of Genius Fish")
      }

    assert(exists(jurgen))
    assert(
      townPlan.businessActor(jurgen.key).exists(he => he.descriptions.size == 3)
    )
  }

}
