package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.EnterpriseArchitectureContext
import com.innovenso.townplanner.model.concepts.views.TechnologyRadar
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class TechnologyRadarSpec extends AnyFlatSpec with GivenWhenThen {
  "a technology radar" should "contain all technologies, as well as containers, systems and platforms that are implemented with them" in new EnterpriseArchitectureContext {
    Given("some systems, with containers and technologies")
    val platform = samples.platform()
    val system1 = samples.system(containingPlatform = Some(platform))
    val system2 = samples.system(containingPlatform = Some(platform))
    When("A technology radar is requested")
    val radar = ea needs TechnologyRadar(title = "Innovenso Technology Radar")
    val compiledRadar = townPlan.technologyRadar(radar.key).get
    Then(
      "the technology radar contains technologies, containers, systems and platforms"
    )
    compiledRadar.technologies.nonEmpty
    compiledRadar.containers.nonEmpty
    compiledRadar.systems.contains(system1)
    compiledRadar.systems.contains(system2)
    compiledRadar.platforms.contains(platform)
  }
}
