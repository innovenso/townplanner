package com.innovenso.townplanner.model.meta

import java.time.LocalDate

case class SomeYear(value: Int) extends Year

case object ThisYear extends Year {
  val value: Int = LocalDate.now().getYear
}

trait Year {
  def value: Int
}
