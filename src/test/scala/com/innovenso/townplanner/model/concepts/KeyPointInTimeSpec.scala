package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Documentation
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey, Title}
import com.innovenso.townplanner.model.test.Factory
import com.innovenso.townplanner.model.{TownPlan, TownPlanFactory}
import org.scalatest.flatspec.AnyFlatSpec

import java.time.LocalDate
import scala.util.Try

class KeyPointInTimeSpec extends AnyFlatSpec {

  "A key point in time" should "be addable to the townplan" in new Factory {
    val townPlanWithKeyPointInTime: Try[(TownPlan, KeyPointInTime)] =
      factory.withKeyPointInTime(date = LocalDate.now(), title = Title("today"))
    val failureBecauseDuplicate: Try[(TownPlan, KeyPointInTime)] =
      factory.withKeyPointInTime(
        date = LocalDate.now(),
        title = Title("also today")
      )
    assert(townPlanWithKeyPointInTime.isSuccess)
    assert(
      townPlanWithKeyPointInTime.get._1.keyPointsInTime
        .contains(LocalDate.now())
    )
    assert(failureBecauseDuplicate.isFailure)
  }

}
