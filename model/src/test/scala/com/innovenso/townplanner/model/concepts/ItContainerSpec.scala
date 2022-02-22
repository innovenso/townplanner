package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  ApiDocumentation,
  BeInvestedIn,
  Description
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItContainerSpec extends AnyFlatSpec with GivenWhenThen {
  "IT Containers" can "be added to the town plan" in new EnterpriseArchitecture {
    Given("a system")
    val itSystem: ItSystem = ea has ItSystem(title = "the system")
    And("a technology")
    val java: LanguageOrFramework = ea has LanguageOrFramework(title = "Java")
    And("a delivery team")
    val team: TeamActor = ea has TeamActor(title = "The A-Team")
    When("a microservice is added to the town plan")
    val ms: Microservice = ea describes Microservice(title = "BFF") as { it =>
      it has Description("Backend for Frontend")
      it should BeInvestedIn()
      it isPartOf itSystem
      it isImplementedBy java
      it isDeliveredBy team
      it has ApiDocumentation(url = "https://townplanner.be")
    }
    And("a database is added to the town plan")
    val db: Database = ea describes Database(title = "The Database") as { it =>
      it isPartOf itSystem
      it isUsedBy ms
    }

    Then("the system exists")
    assert(exists(itSystem))
    And("the microservice exists")
    assert(exists(ms))
    And("the database exists")
    assert(exists(db))
    And("the town plan has the correct relationships")
    assert(townPlan.relationships.size == 5)
    And("the system has the correct number of containers")
    assert(townPlan.containers(itSystem).size == 2)
    And("the containers have documentation links")
    assert(townPlan.container(ms.key).exists(_.apiDocumentationLinks.nonEmpty))
  }

}
