package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Description
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import java.time.LocalDate

class KeyPointInTimeSpec extends AnyFlatSpec with GivenWhenThen {
  "Key Points in Time" can "be added to the townplan" in new EnterpriseArchitecture {
    ea has KeyPointInTime(LocalDate.now(), "today")
    ea describes KeyPointInTime(LocalDate.of(2022, 7, 1), "pi8") as { it =>
      it has Description("the start of the summer")
    }

    assert(townPlan.pointsInTime.size == 2)
  }

}
