package com.innovenso.townplanner.model.meta

trait Aspect {
  def name: String
  def description: String
}

case object ActiveStructure extends Aspect {
  val name = "Active Structure"
  val description = "Structural concepts that display actual behavior"
}

case object PassiveStructure extends Aspect {
  val name = "Passive Structure"
  val description = "Concepts on which behavior is performed"
}

case object Behavior extends Aspect {
  val name = "Behavior"
  val description =
    "The actual behavior performed by active structure concepts on passive structure concepts"
}

case object NoStructure extends Aspect {
  val name = "None"
  val description =
    "Model components that do not belong to one of the 3 structure aspects"
}
