package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Description, Key, SortKey}

case class ArchitectureVerdict(
    architectureVerdictType: ArchitectureVerdictType,
    description: Description = Description(None)
) extends Property {
  val key: Key = Key()
  val sortKey: SortKey = SortKey(None)
  val canBePlural: Boolean = false
}

trait HasArchitectureVerdict extends HasProperties {
  def architectureVerdict: ArchitectureVerdict =
    props(classOf[ArchitectureVerdict]).headOption
      .getOrElse(ArchitectureVerdict(UnknownVerdict, Description(None)))
}

trait ArchitectureVerdictType {
  def name: String
  def radarCircle: Int
}

case object Tolerate extends ArchitectureVerdictType {
  val name: String = "Tolerate"
  val radarCircle: Int = 2
}

case object Invest extends ArchitectureVerdictType {
  val name: String = "Invest"
  val radarCircle: Int = 1
}

case object Migrate extends ArchitectureVerdictType {
  val name: String = "Migrate"
  val radarCircle: Int = 3
}

case object Eliminate extends ArchitectureVerdictType {
  val name: String = "Eliminate"
  val radarCircle: Int = 4
}

case object UnknownVerdict extends ArchitectureVerdictType {
  val name: String = "Unknown"
  val radarCircle: Int = 0
}

trait CanConfigureArchitectureVerdict[
    ModelComponentType <: HasArchitectureVerdict
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(verdict: ArchitectureVerdict): HasArchitectureVerdict =
    propertyAdder.withProperty(modelComponent, verdict)
}
