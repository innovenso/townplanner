package com.innovenso.townplan.io.context

sealed trait OutputStatus {
  def name: String
}

object Success extends OutputStatus {
  val name = "SUCCESS"
}

case class Failure(reason: String) extends OutputStatus {
  val name = "FAILURE"
}
