package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

abstract class Throughput extends Property {
  val key: Key = Key()
  val sortKey: SortKey = SortKey.next
  val canBePlural: Boolean = false
  def description: String
}

case class Volume(
    description: String
) extends Throughput

case class Frequency(
    description: String
) extends Throughput

trait HasThroughput extends HasProperties {
  def volume: Option[Volume] = props(classOf[Volume]).headOption
  def frequency: Option[Frequency] = props(classOf[Frequency]).headOption
  def withVolume(volume: Volume): HasProperties =
    withProperty(volume)
  def withFrequency(frequency: Frequency): HasProperties =
    withProperty(frequency)
}

trait CanConfigureThroughput[ModelComponentType <: HasThroughput] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(volume: Volume): HasThroughput =
    propertyAdder.withProperty(modelComponent, volume)
  def has(frequency: Frequency): HasThroughput =
    propertyAdder.withProperty(modelComponent, frequency)
}
