package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.Key

abstract class ArchitectureVerdict extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = false

  def name: String

  def radarCircle: Int
}

case class BeTolerated(
    description: String = ""
) extends ArchitectureVerdict {
  val name: String = "Tolerate"
  val radarCircle: Int = 2
}

case class BeInvestedIn(
    description: String = ""
) extends ArchitectureVerdict {
  val name: String = "Invest"
  val radarCircle: Int = 1
}

case class BeMigrated(
    description: String = ""
) extends ArchitectureVerdict {
  val name: String = "Migrate"
  val radarCircle: Int = 3
}

case class BeEliminated(
    description: String = ""
) extends ArchitectureVerdict {
  val name: String = "Eliminate"
  val radarCircle: Int = 4
}

case class DetermineLifecyle(description: String = "")
    extends ArchitectureVerdict {
  val name: String = "Unknown"
  val radarCircle: Int = 0
}

trait HasArchitectureVerdict extends HasProperties {
  def architectureVerdict: ArchitectureVerdict =
    props(classOf[ArchitectureVerdict]).headOption
      .getOrElse(DetermineLifecyle())
}

trait CanConfigureArchitectureVerdict[
    ModelComponentType <: HasArchitectureVerdict
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def should(verdict: ArchitectureVerdict): ModelComponentType =
    propertyAdder.withProperty(modelComponent, verdict)
}
