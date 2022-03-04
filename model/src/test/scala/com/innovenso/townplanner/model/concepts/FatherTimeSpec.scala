package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.meta.{ADay, Day}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class FatherTimeSpec extends AnyFlatSpec with GivenWhenThen {
  "An IT System" can "have a lifecycle" in new EnterpriseArchitectureContext {
    val theSystem: ItSystem = ea describes ItSystem(title = "The System") as {
      it =>
        it is Conceived() on Day(2020, 1, 1)
        it has StartedDevelopment() on Day(2020, 7, 1)
        it has GoneToPreproduction() on Day(2021, 1, 1)
        it has GoneToProduction() on Day(2021, 7, 1)
        it has Retired() on Day(2022, 1, 1)
        it is Decommissioned() on Day(2022, 7, 1)
    }

    val beforeAnything: ADay =
      Day(2019, 1, 1)
    val beforeDevelopment: ADay =
      Day(2020, 3, 1)
    val inDevelopment: ADay =
      Day(2020, 10, 1)
    val inStaging: ADay =
      Day(2021, 3, 1)
    val inProduction: ADay =
      Day(2021, 10, 1)
    val phasingOut: ADay =
      Day(2022, 3, 1)
    val gone: ADay = Day(2022, 10, 1)

    assert(theSystem.isNotEvenPlanned(beforeAnything))
    assert(theSystem.isPlanned(beforeDevelopment))
    assert(theSystem.isPlanned(inDevelopment))
    assert(theSystem.isPlanned(inStaging))
    assert(theSystem.isActive(inProduction))
    assert(theSystem.isPhasingOut(phasingOut))
    assert(theSystem.isDecommissioned(gone))
  }
}
