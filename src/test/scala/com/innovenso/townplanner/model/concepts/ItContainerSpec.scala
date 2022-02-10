package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  BeInvestedIn,
  Description
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItContainerSpec extends AnyFlatSpec with GivenWhenThen {
  "IT Containers" can "be added to the town plan" in new EnterpriseArchitecture {
    val itSystem: ItSystem = ea has ItSystem(title = "the system")
    val java: LanguageOrFramework = ea has LanguageOrFramework(title = "Java")
    val team: TeamActor = ea has TeamActor(title = "The A-Team")
    val ms: Microservice = ea describes Microservice(title = "BFF") as { it =>
      it has Description("Backend for Frontend")
      it should BeInvestedIn()
      it isPartOf itSystem
      it isImplementedBy java
      it isDeliveredBy team
    }
    val db: Database = ea describes Database(title = "The Database") as { it =>
      it isPartOf itSystem
      it isUsedBy ms
    }

    assert(exists(itSystem))
    assert(exists(ms))
    assert(exists(db))
    assert(townPlan.containers(itSystem).head == ms)
    assert(townPlan.relationships.size == 5)
    assert(townPlan.containers(itSystem).size == 2)
    assert(townPlan.technologies(ms).head == java)
  }

}
