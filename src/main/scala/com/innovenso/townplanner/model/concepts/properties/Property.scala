package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.language.{
  CanAddModelComponents,
  ModelComponent
}
import com.innovenso.townplanner.model.meta.{Key, SortKey}

trait Property {
  def key: Key
  def canBePlural: Boolean
}

trait HasProperties extends ModelComponent {
  def properties: Map[Key, Property]
  def withProperty(property: Property): HasProperties
  def withProperties(properties: List[Property]): HasProperties = if (
    properties.isEmpty
  ) this
  else this.withProperty(properties.head).withProperties(properties.tail)

  def props[PropertyType <: Property](
      shouldBeOfClass: Class[PropertyType]
  ): List[PropertyType] =
    properties.values
      .filter(property => is(property, shouldBeOfClass))
      .map(property => as(property, shouldBeOfClass))
      .toList

  def prop[PropertyType <: Property](
      key: Key,
      shouldBeOfClass: Class[PropertyType]
  ): Option[PropertyType] =
    properties
      .get(key)
      .filter(property => is(property, shouldBeOfClass))
      .map(property => as(property, shouldBeOfClass))

  private def is(
      property: Property,
      shouldBeOfClass: Class[_ <: Property]
  ): Boolean = shouldBeOfClass.isInstance(property)

  private def as[PropertyType <: Property](
      property: Property,
      shouldBeOfClass: Class[PropertyType]
  ): PropertyType = shouldBeOfClass.cast(property)
}

trait CanAddProperties extends CanAddModelComponents {

  def withProperty[ModelComponentType <: HasProperties](
      modelComponent: ModelComponentType,
      property: Property
  ): ModelComponentType = {
    val modelComponentOption: Option[ModelComponentType] =
      townPlan.component(modelComponent.key, modelComponent.getClass)
    if (modelComponentOption.isEmpty)
      throw new IllegalArgumentException(
        s"the town plan does not contain a model component with key ${modelComponent.key.value} that ${modelComponent.getClass.getSimpleName}"
      )
    else if (
      modelComponentOption.get
        .props(property.getClass)
        .nonEmpty && !property.canBePlural
    )
      throw new IllegalArgumentException(
        s"the component ${modelComponent.key.value} already has a property of type ${property.getClass.getSimpleName}"
      )
    else {
      println(s"setting property ${property} on component ${modelComponent}")
      val updatedModelComponent =
        modelComponentOption.get.withProperty(property)
      this.townPlan = townPlan.copy(
        townPlan.modelComponents + (modelComponentOption.get.key -> updatedModelComponent)
      )
      this.townPlan
        .component(updatedModelComponent.key, modelComponent.getClass)
        .get
    }
  }

}
