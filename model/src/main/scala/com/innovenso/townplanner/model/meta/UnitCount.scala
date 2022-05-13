package com.innovenso.townplanner.model.meta

case class UnitCount(count: Double) {
  override def toString: String = f"${count}%2.2f"
}
