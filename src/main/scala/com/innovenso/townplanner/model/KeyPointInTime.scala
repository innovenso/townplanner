package com.innovenso.townplanner.model

import com.innovenso.townplanner.model.meta.Title

import java.time.LocalDate

case class KeyPointInTime(date: LocalDate, title: Title, diagramsNeeded: Boolean) {
  def isToday: Boolean = LocalDate.now().equals(date)
  def isFuture: Boolean = LocalDate.now().isBefore(date)
  def isPast: Boolean = LocalDate.now().isAfter(date)
}
