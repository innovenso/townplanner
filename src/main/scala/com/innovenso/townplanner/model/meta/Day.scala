package com.innovenso.townplanner.model.meta

import java.time.LocalDate

case class Day(year: Int, month: Int, day: Int) extends ADay

case object Today extends ADay {
  val todayLocalDate: LocalDate = LocalDate.now()
  val year: Int = todayLocalDate.getYear
  val month: Int = todayLocalDate.getMonthValue
  val day: Int = todayLocalDate.getDayOfMonth
}

sealed trait ADay {
  def year: Int
  def month: Int
  def day: Int

  def is(other: LocalDate): Boolean = toLocalDate.isEqual(other)
  def is(other: ADay): Boolean = toLocalDate.isEqual(other.toLocalDate)
  def isBefore(other: ADay): Boolean = toLocalDate.isBefore(other.toLocalDate)
  def isAfter(other: ADay): Boolean = toLocalDate.isAfter(other.toLocalDate)
  def isAfterOrEqual(other: ADay): Boolean = toLocalDate.isAfter(
    other.toLocalDate
  ) || toLocalDate.isEqual(other.toLocalDate)

  private def toLocalDate: LocalDate = LocalDate.of(year, month, day)
}
