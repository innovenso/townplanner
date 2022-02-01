package com.innovenso.townplanner.model.meta

case class SortKey(value: Option[String]) extends Ordered[SortKey] {
  override def compare(that: SortKey): Int =
    value.getOrElse("").compareTo(that.value.getOrElse(""))
}
