package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.{TownPlan, TownPlanFactory}
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey, Title}
import org.scalatest.flatspec.AnyFlatSpec

import java.time.LocalDate
import scala.util.Try

class TownPlanSpec extends AnyFlatSpec {
  trait Factory {
    val factory: TownPlanFactory = new TownPlanFactory
  }

  "An Enterprise" should "be addable to the townplan" in new Factory {
    assert(factory.townPlan.modelComponents.isEmpty)
    assert(factory.townPlan.keyPointsInTime.isEmpty)
    val emptyTownPlan: TownPlan = factory.townPlan
    val townPlanWithEnterprise: Try[TownPlan] =
      factory.withEnterprise(title = Title("Innovenso BV"))
    assert(townPlanWithEnterprise.isSuccess)
    val townPlanWithSecondEnterprise: Try[TownPlan] = factory.withEnterprise(
      key = Key("apple"),
      sortKey = SortKey(Some("AAP")),
      title = Title("Apple"),
      description = Description(Some("creator of the iPhone and other stuff"))
    )
    val failureBecauseDuplicate: Try[TownPlan] =
      factory.withEnterprise(key = Key("apple"), title = Title("Pear"))
    assert(failureBecauseDuplicate.isFailure)
    assert(townPlanWithSecondEnterprise.isSuccess)
    assert(townPlanWithEnterprise.get != townPlanWithSecondEnterprise.get)
    assert(townPlanWithEnterprise.get != emptyTownPlan)
    assert(townPlanWithSecondEnterprise.get == factory.townPlan)
  }

  "A key point in time" should "be addable to the townplan" in new Factory {
    val townPlanWithKeyPointInTime: Try[TownPlan] =
      factory.withKeyPointInTime(date = LocalDate.now(), title = Title("today"))
    val failureBecauseDuplicate: Try[TownPlan] = factory.withKeyPointInTime(
      date = LocalDate.now(),
      title = Title("also today")
    )
    assert(townPlanWithKeyPointInTime.isSuccess)
    assert(
      townPlanWithKeyPointInTime.get.keyPointsInTime.contains(LocalDate.now())
    )
    assert(failureBecauseDuplicate.isFailure)
  }
}
