package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

abstract class ArchitectureVerdict extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = false
  val sortKey: SortKey = SortKey.next
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
  def isToBeEliminated: Boolean = architectureVerdict.isInstanceOf[BeEliminated]

  def isToBeTolerated: Boolean = architectureVerdict.isInstanceOf[BeTolerated]

  def isToBeInvestedIn: Boolean = architectureVerdict.isInstanceOf[BeInvestedIn]

  def architectureVerdict: ArchitectureVerdict =
    props(classOf[ArchitectureVerdict]).headOption
      .getOrElse(DetermineLifecyle())

  def isToBeMigrated: Boolean = architectureVerdict.isInstanceOf[BeMigrated]
}

trait CanConfigureArchitectureVerdict[
    ModelComponentType <: HasArchitectureVerdict
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def should(verdict: ArchitectureVerdict): ModelComponentType =
    propertyAdder.withProperty(modelComponent, verdict)
}
