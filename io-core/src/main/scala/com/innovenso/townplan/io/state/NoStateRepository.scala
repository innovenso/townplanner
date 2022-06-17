package com.innovenso.townplan.io.state
import com.innovenso.townplanner.model.language.CompiledView

case class NoStateRepository() extends StateRepository {
  override def hasChanged(view: CompiledView[_], writer: String): Boolean = true

  override def touch(view: CompiledView[_], writer: String): Unit = {}
}
