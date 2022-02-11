package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

case class ExternalId(
    id: String,
    externalSystemName: String
) extends Property {
  val key: Key = Key()
  val sortKey: SortKey = SortKey.next
  val canBePlural: Boolean = true
}

trait HasExternalIds extends HasProperties {
  def externalIds: List[ExternalId] = props(classOf[ExternalId])
  def externalId(key: Key): Option[ExternalId] =
    prop(key, classOf[ExternalId])
  def withExternalId(externalId: ExternalId): HasProperties =
    withProperty(externalId)
}

case class ExternalIdConfigurer(
    modelComponent: HasExternalIds,
    externalId: String,
    propertyAdder: CanAddProperties
) {
  def on(externalSystemName: String): HasExternalIds =
    propertyAdder.withProperty(
      modelComponent,
      ExternalId(externalId, externalSystemName)
    )

}

trait CanConfigureExternalIds[ModelComponentType <: HasExternalIds] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def isIdentifiedAs(externalId: String): ExternalIdConfigurer =
    ExternalIdConfigurer(modelComponent, externalId, propertyAdder)
}
