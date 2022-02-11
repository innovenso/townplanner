package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  Conceived,
  Decommissioned,
  GoneToPreproduction,
  GoneToProduction,
  Retired,
  StartedDevelopment
}
import com.innovenso.townplanner.model.meta.Day
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class FatherTimeSpec extends AnyFlatSpec with GivenWhenThen {
  "An IT System" can "have a lifecycle" in new EnterpriseArchitecture {
    val theSystem: ItSystem = ea describes ItSystem(title = "The System") as {
      it =>
        it is Conceived() on Day(2020, 1, 1)
        it has StartedDevelopment() on Day(2020, 7, 1)
        it has GoneToPreproduction() on Day(2021, 1, 1)
        it has GoneToProduction() on Day(2021, 7, 1)
        it has Retired() on Day(2022, 1, 1)
        it is Decommissioned() on Day(2022, 7, 1)
    }

    val beforeAnything: KeyPointInTime =
      KeyPointInTime(Day(2019, 1, 1), "before anything")
    val beforeDevelopment: KeyPointInTime =
      KeyPointInTime(Day(2020, 3, 1), "before development")
    val inDevelopment: KeyPointInTime =
      KeyPointInTime(Day(2020, 10, 1), "in development")
    val inStaging: KeyPointInTime =
      KeyPointInTime(Day(2021, 3, 1), "in staging")
    val inProduction: KeyPointInTime =
      KeyPointInTime(Day(2021, 10, 1), "in production")
    val phasingOut: KeyPointInTime =
      KeyPointInTime(Day(2022, 3, 1), "phasing out")
    val gone: KeyPointInTime = KeyPointInTime(Day(2022, 10, 1), "gone")

    assert(theSystem.isNotEvenPlanned(beforeAnything))
    assert(theSystem.isPlanned(beforeDevelopment))
    assert(theSystem.isPlanned(inDevelopment))
    assert(theSystem.isPlanned(inStaging))
    assert(theSystem.isActive(inProduction))
    assert(theSystem.isPhasingOut(phasingOut))
    assert(theSystem.isDecommissioned(gone))
  }
}
