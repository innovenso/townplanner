package com.innovenso.townplanner.model.meta

import com.innovenso.townplanner.model.meta.Today.todayLocalDate

import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

  def +(days: Long): ADay = {
    val localDate = LocalDate.of(year, month, day).plusDays(days)
    Day(localDate.getYear, localDate.getMonthValue, localDate.getDayOfMonth)
  }

  def -(days: Long): ADay = {
    val localDate = LocalDate.of(year, month, day).minusDays(days)
    Day(localDate.getYear, localDate.getMonthValue, localDate.getDayOfMonth)
  }

  def nextDay: ADay = this + 1
  def previousDay: ADay = this - 1

  def is(other: LocalDate): Boolean = toLocalDate.isEqual(other)

  private def toLocalDate: LocalDate = LocalDate.of(year, month, day)

  def is(other: ADay): Boolean = toLocalDate.isEqual(other.toLocalDate)

  def isBefore(other: ADay): Boolean = toLocalDate.isBefore(other.toLocalDate)

  def isBeforeOrEqual(other: ADay): Boolean = toLocalDate.isBefore(
    other.toLocalDate
  ) || toLocalDate.isEqual(other.toLocalDate)

  def isAfter(other: ADay): Boolean = toLocalDate.isAfter(other.toLocalDate)

  def isAfterOrEqual(other: ADay): Boolean = toLocalDate.isAfter(
    other.toLocalDate
  ) || toLocalDate.isEqual(other.toLocalDate)

  override def toString: String =
    toLocalDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
  def format(formatter: DateTimeFormatter): String =
    toLocalDate.format(formatter)
}
