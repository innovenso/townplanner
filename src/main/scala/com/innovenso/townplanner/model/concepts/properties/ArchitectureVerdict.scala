package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey}

import scala.util.Try

case class ArchitectureVerdict(
    description: Description,
    architectureVerdictType: ArchitectureVerdictType
) extends Property {
  val key: Key = Key()
  val sortKey: SortKey = SortKey(None)
  val canBePlural: Boolean = false
}

trait HasArchitectureVerdict extends HasProperties {
  def architectureVerdict: ArchitectureVerdict =
    props(classOf[ArchitectureVerdict]).headOption
      .getOrElse(ArchitectureVerdict(Description(None), UnknownVerdict))
}

trait CanSetArchitectureVerdict extends CanAddProperties {
  def withArchitectureVerdict(
      key: Key,
      architectureVerdict: ArchitectureVerdict
  ): Try[(TownPlan, HasArchitectureVerdict)] =
    withProperty(key, architectureVerdict, classOf[HasArchitectureVerdict])
  def withArchitectureVerdict[ModelComponentType <: HasArchitectureVerdict](
      modelComponent: ModelComponentType,
      architectureVerdict: ArchitectureVerdict
  ): Try[(TownPlan, ModelComponentType)] =
    withProperty(modelComponent, architectureVerdict)
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
