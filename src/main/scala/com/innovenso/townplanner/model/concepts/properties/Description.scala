package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

case class Description(
    value: String
) extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = true
}

trait HasDescription extends HasProperties {
  def descriptions: List[Description] = props(classOf[Description])
  def description(key: Key): Option[Description] =
    prop(key, classOf[Description])
  def withDescription(description: Description): HasProperties =
    withProperty(description)
}

trait CanConfigureDescription[ModelComponentType <: HasDescription] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(description: Description): HasDescription =
    propertyAdder.withProperty(modelComponent, description)
}
