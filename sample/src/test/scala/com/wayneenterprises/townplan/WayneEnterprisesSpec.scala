package com.wayneenterprises.townplan

import com.innovenso.townplanner.model.concepts.views.SystemContainerView
import com.wayneenterprises.townplan.application.{BuildingBlocks, Systems}
import com.wayneenterprises.townplan.business.Actors
import com.wayneenterprises.townplan.strategy.{
  BusinessCapabilities,
  Enterprises
}
import com.wayneenterprises.townplan.technology.TechnologyRadar
import com.wayneenterprises.townplan.views.Views
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class WayneEnterprisesSpec extends AnyFlatSpec with GivenWhenThen {
  "Wayne Enterprises" should "have all the requested diagrams" in new DiagramIO {
    Given("the Wayne Enterprises town plan")
    implicit val enterprises: Enterprises = Enterprises()
    implicit val capabilities: BusinessCapabilities = BusinessCapabilities()
    implicit val technologyRadar: TechnologyRadar = TechnologyRadar()
    implicit val actors: Actors = Actors()
    implicit val buildingBlocks: BuildingBlocks = BuildingBlocks()
    implicit val systems: Systems = Systems()

    When("views are requested")
    val bcmsToday: SystemContainerView =
      ea needs SystemContainerView(forSystem = systems.bcms)

    Then("diagrams are rendered")
    specificationExists(townPlan.systemContainerView(bcmsToday.key))
    diagramsAreWritten(bcmsToday.key)
  }
}
