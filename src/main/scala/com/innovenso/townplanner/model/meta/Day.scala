package com.innovenso.townplanner.model.meta

import java.time.LocalDate

case class Day(year: Int, month: Int, day: Int) extends ADay

object Today extends ADay {
  val todayLocalDate: LocalDate = LocalDate.now()
  val year: Int = todayLocalDate.getYear
  val month: Int = todayLocalDate.getMonthValue
  val day: Int = todayLocalDate.getDayOfMonth
}

object InTheFuture extends ADay {
  val futureLocalDate: LocalDate = LocalDate.MAX
  val year: Int = futureLocalDate.getYear
  val month: Int = futureLocalDate.getMonthValue
  val day: Int = futureLocalDate.getDayOfMonth
}

object InThePast extends ADay {
  val pastLocalDate: LocalDate = LocalDate.MIN
  val year: Int = pastLocalDate.getYear
  val month: Int = pastLocalDate.getMonthValue
  val day: Int = pastLocalDate.getDayOfMonth
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
