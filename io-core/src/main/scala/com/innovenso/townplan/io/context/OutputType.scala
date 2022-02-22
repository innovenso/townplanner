package com.innovenso.townplan.io.context

trait OutputType {
  def title: String
  def description: String
}

object Book extends OutputType {
  val title = "Book"
  val description = "Book containing the full town plan"
}
