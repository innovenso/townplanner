package com.innovenso.townplanner.model.language

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.meta.{Key, ModelComponentType, SortKey}

trait ModelComponent {
  def key: Key
  def sortKey: SortKey
  def modelComponentType: ModelComponentType
}

trait HasModelComponents {
  def modelComponents: Map[Key, ModelComponent]

  def has[ModelComponentType <: ModelComponent](
      modelComponent: ModelComponentType
  ): Boolean = component(modelComponent.key, modelComponent.getClass).isDefined

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

  def components[A <: ModelComponent](shouldBeOfClass: Class[A]): List[A] =
    modelComponents.values
      .filter(modelComponent => is(modelComponent, shouldBeOfClass))
      .map(modelComponent => as(modelComponent, shouldBeOfClass))
      .toList
      .distinct
      .sortWith(_.sortKey < _.sortKey)
}

trait CanAddModelComponents {
  var townPlan: TownPlan = TownPlan(
    Map.empty[Key, ModelComponent]
  )

  def has[ModelComponentType <: ModelComponent](
      modelComponent: ModelComponentType
  ): ModelComponentType =
    if (townPlan.modelComponents.contains(modelComponent.key))
      throw new IllegalArgumentException(
        s"the townplan already contains a component with key ${modelComponent.key} of type ${modelComponent.modelComponentType.value}"
      )
    else {
      this.townPlan = townPlan.copy(
        townPlan.modelComponents + (modelComponent.key -> modelComponent)
      )
      modelComponent
    }
}
