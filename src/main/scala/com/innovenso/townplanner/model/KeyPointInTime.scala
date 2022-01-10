package com.innovenso.townplanner.model

import com.innovenso.townplanner.model.meta.Title

import java.time.LocalDate

case class KeyPointInTime(localDate: LocalDate, title: Title, diagramsNeeded: Boolean) {
  def isToday: Boolean = LocalDate.now().equals(localDate)
}
