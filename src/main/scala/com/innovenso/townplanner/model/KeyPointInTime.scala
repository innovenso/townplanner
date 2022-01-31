package com.innovenso.townplanner.model

import com.innovenso.townplanner.model.meta.Title

import java.time.LocalDate

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
