package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Description, Key, SortKey}

case class Documentation(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    description: Description
) extends Property {
  val canBePlural: Boolean = true
}

trait HasDocumentation extends HasProperties {
  def documentations: List[Documentation] = props(classOf[Documentation])
  def documentation(key: Key): Option[Documentation] =
    prop(key, classOf[Documentation])
  def withDocumentation(documentation: Documentation): HasProperties =
    withProperty(documentation)
}

trait CanConfigureDocumentation[ModelComponentType <: HasDocumentation] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(documentation: Documentation): HasDocumentation =
    propertyAdder.withProperty(modelComponent, documentation)
}
