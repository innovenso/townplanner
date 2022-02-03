package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.meta.Title
import com.innovenso.townplanner.model.test.Factory
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Try

class ItContainerSpec extends AnyFlatSpec with GivenWhenThen {
  "An IT Container" should "be addable to a town plan" in new Factory {
    val result: Try[(TownPlan, ItContainer)] =
      factory.withItContainer(title = Title("The Container"))
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val container: ItContainer = result.get._2
    assert(townPlan.has(container))
  }
}
