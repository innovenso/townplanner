package com.innovenso.townplan.io.model

import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ColorSchemeSpec extends AnyFlatSpec with GivenWhenThen {
  "a color scheme" should "be exportable to a map" in {
    Given("a color scheme")
    val colorScheme = IOConfiguration.colorScheme
    When("it is exported to a map")
    val colorMap = colorScheme.toMap
    Then("it has all the standard colors")
    assert(colorMap.contains("innovensoAccent1"))
    assert(colorMap.contains("innovensoAccent20"))
    assert(colorMap.contains("innovensoText"))
  }
}
