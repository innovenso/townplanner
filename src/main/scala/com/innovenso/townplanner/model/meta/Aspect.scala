package com.innovenso.townplanner.model.meta

trait Aspect {
  def name: String
  def description: String
}

object ActiveStructure extends Aspect {
  val name = "Active Structure"
  val description = "Structural concepts that display actual behavior"
}

object PassiveStructure extends Aspect {
  val name = "Passive Structure"
  val description = "Concepts on which behavior is performed"
}

object Behavior extends Aspect {
  val name = "Behavior"
  val description = "The actual behavior performed by active structure concepts on passive structure concepts"
}
