package com.innovenso.townplanner.model.factory

import com.innovenso.townplanner.model.{KeyPointInTime, TownPlan}
import com.innovenso.townplanner.model.meta.Title

import java.time.LocalDate
import scala.util.{Failure, Success, Try}

trait CanAddKeyPointsInTime extends CanManipulateTownPlan {
  def withKeyPointInTime(
      date: LocalDate,
      title: Title,
      diagramsNeeded: Boolean = true
  ): Try[TownPlan] = {
    if (townPlan.keyPointsInTime.contains(date))
      Failure(
        new IllegalArgumentException(
          s"town plan already contains a key point in time for date ${date}"
        )
      )
    else {
      this.townPlan = townPlan.copy(keyPointsInTime =
        townPlan.keyPointsInTime + (date -> KeyPointInTime(
          date,
          title,
          diagramsNeeded
        ))
      )
      Success(this.townPlan)
    }
  }
}
