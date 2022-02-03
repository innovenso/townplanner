package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.meta.Title
import com.innovenso.townplanner.model.test.Factory
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Try

class ItPlatformSpec extends AnyFlatSpec with GivenWhenThen {
  "An IT Platform" should "be addable to a town plan" in new Factory {
    val result: Try[(TownPlan, ItPlatform)] =
      factory.withItPlatform(title = Title("The Platform"))
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val platform: ItPlatform = result.get._2
    assert(townPlan.has(platform))
  }
}
