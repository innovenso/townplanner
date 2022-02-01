package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.{CanManipulateTownPlan, TownPlan}
import com.innovenso.townplanner.model.meta.Title

import java.time.LocalDate
import scala.util.{Failure, Success, Try}

case class KeyPointInTime(
    date: LocalDate,
    title: Title,
    diagramsNeeded: Boolean
) {
  def isToday: Boolean = LocalDate.now().equals(date)
  def isFuture: Boolean = LocalDate.now().isBefore(date)
  def isPast: Boolean = LocalDate.now().isAfter(date)
}

trait HasKeyPointsInTime {
  def keyPointsInTime: Map[LocalDate, KeyPointInTime]

  def pointsInTime: List[KeyPointInTime] = keyPointsInTime.values.toList
    .sortWith(_.date.toEpochDay < _.date.toEpochDay)
  def pointInTime(localDate: LocalDate): KeyPointInTime = keyPointsInTime(
    localDate
  )
}

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
