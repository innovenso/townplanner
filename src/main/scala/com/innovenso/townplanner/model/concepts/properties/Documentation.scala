package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey}

import scala.util.Try

case class Documentation(key: Key, sortKey: SortKey, description: Description)
    extends Property

trait HasDocumentation extends HasProperties {
  def documentations: List[Documentation] = props(classOf[Documentation])
  def documentation(key: Key): Option[Documentation] =
    prop(key, classOf[Documentation])
  def withDocumentation(documentation: Documentation): HasProperties =
    withProperty(documentation)
}

trait CanAddDocumentations extends CanAddProperties {
  def withDocumentation(
      key: Key,
      documentation: Documentation
  ): Try[TownPlan] = withProperty(key, documentation, classOf[HasDocumentation])
  def withDocumentation[ModelComponentType <: HasDocumentation](
      modelComponent: ModelComponentType,
      documentation: Documentation
  ): Try[TownPlan] = withProperty(modelComponent, documentation)
}
