package com.innovenso.townplan.io.state

import com.innovenso.townplan.io.context.OutputType
import com.innovenso.townplanner.model.language.CompiledView

import java.io.File
import scala.util.Try

trait StateRepository {
  def hasChanged(view: CompiledView[_], writer: String): Boolean
  def touch(view: CompiledView[_], writer: String): Unit
}
