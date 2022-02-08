package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.language.CanAddModelComponents
import com.innovenso.townplanner.model.meta.Title

import java.time.LocalDate

case class KeyPointInTime(
    date: LocalDate,
    title: String,
    diagramsNeeded: Boolean = true
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

trait CanAddKeyPointsInTime extends CanAddModelComponents {
  def withKeyPointInTime(
      date: LocalDate,
      title: String,
      diagramsNeeded: Boolean = true
  ): KeyPointInTime = {
    if (townPlan.keyPointsInTime.contains(date))
      throw new IllegalArgumentException(
        s"town plan already contains a key point in time for date ${date}"
      )
    else {
      val keyPointInTime: KeyPointInTime = KeyPointInTime(
        date,
        title,
        diagramsNeeded
      )
      this.townPlan = townPlan.copy(keyPointsInTime =
        townPlan.keyPointsInTime + (date -> keyPointInTime)
      )
      keyPointInTime
    }
  }
}
