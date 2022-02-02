package com.innovenso.townplanner.model.language

import com.innovenso.townplanner.model.meta.{Key, ModelComponentType, SortKey}

trait ModelComponent {
  def key: Key
  def sortKey: SortKey
  def modelComponentType: ModelComponentType
}

trait HasModelComponents {
  def modelComponents: Map[Key, ModelComponent]
  def components[A <: ModelComponent](shouldBeOfClass: Class[A]): List[A] =
    modelComponents.values
      .filter(modelComponent => is(modelComponent, shouldBeOfClass))
      .map(modelComponent => as(modelComponent, shouldBeOfClass))
      .toList
      .sortWith(_.sortKey < _.sortKey)
  def component[A <: ModelComponent](
      key: Key,
      shouldBeOfClass: Class[A]
  ): Option[A] =
    modelComponents
      .get(key)
      .filter(modelComponent => is(modelComponent, shouldBeOfClass))
      .map(modelComponent => as(modelComponent, shouldBeOfClass))
  private def is(
      modelComponent: ModelComponent,
      shouldBeOfClass: Class[_ <: ModelComponent]
  ): Boolean = shouldBeOfClass.isInstance(modelComponent)
  private def as[A <: ModelComponent](
      modelComponent: ModelComponent,
      shouldBeOfClass: Class[A]
  ): A = shouldBeOfClass.cast(modelComponent)
}
