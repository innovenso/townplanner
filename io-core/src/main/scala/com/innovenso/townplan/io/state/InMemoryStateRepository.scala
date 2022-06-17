package com.innovenso.townplan.io.state

import com.innovenso.townplanner.model.language.CompiledView
import org.apache.commons.codec.digest.DigestUtils

case class InMemoryStateRepository() extends StateRepository {
  private var state: Set[String] = Set()

  override def hasChanged(view: CompiledView[_], writer: String): Boolean = {
    val changed = !state.contains(getHash(view, writer))
    println(s"changed? ${changed}")
    changed
  }

  override def touch(view: CompiledView[_], writer: String): Unit = {
    this.state = state + getHash(view, writer)
    state.foreach(println(_))
  }

  def getHash(view: CompiledView[_], writer: String): String = {
    val hash = writer + " " +
      DigestUtils.sha256Hex(view.toString)
    println(hash)
    hash
  }

}
