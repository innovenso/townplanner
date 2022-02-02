package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.{CanManipulateTownPlan, TownPlan}
import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.meta.{Key, SortKey}

import scala.util.{Failure, Success, Try}

trait Property {
  def key: Key
  def sortKey: SortKey
}

trait HasProperties extends ModelComponent {
  def properties: Map[Key, Property]
  def withProperty(property: Property): HasProperties

  def props[PropertyType <: Property](
      shouldBeOfClass: Class[PropertyType]
  ): List[PropertyType] =
    properties.values
      .filter(property => is(property, shouldBeOfClass))
      .map(property => as(property, shouldBeOfClass))
      .toList
      .sortWith(_.sortKey < _.sortKey)

  private def is(
      property: Property,
      shouldBeOfClass: Class[_ <: Property]
  ): Boolean = shouldBeOfClass.isInstance(property)

  private def as[PropertyType <: Property](
      property: Property,
      shouldBeOfClass: Class[PropertyType]
  ): PropertyType = shouldBeOfClass.cast(property)

  def prop[PropertyType <: Property](
      key: Key,
      shouldBeOfClass: Class[PropertyType]
  ): Option[PropertyType] =
    properties
      .get(key)
      .filter(property => is(property, shouldBeOfClass))
      .map(property => as(property, shouldBeOfClass))

}

trait CanAddProperties extends CanManipulateTownPlan {
  def withProperty[HasPropertyType <: HasProperties](
      modelComponent: HasPropertyType,
      property: Property
  ): Try[TownPlan] =
    withProperty(modelComponent.key, property, modelComponent.getClass)

  def withProperty[HasPropertyType <: HasProperties](
      key: Key,
      property: Property,
      hasPropertyType: Class[HasPropertyType]
  ): Try[TownPlan] = {
    val modelComponentOption: Option[HasPropertyType] =
      townPlan.component(key, hasPropertyType)
    if (modelComponentOption.isEmpty)
      Failure(
        new IllegalArgumentException(
          s"the town plan does not contain a model component with key ${key} that ${hasPropertyType.getSimpleName}"
        )
      )
    else {
      this.townPlan = townPlan.copy(
        townPlan.modelComponents + (modelComponentOption.get.key -> modelComponentOption.get
          .withProperty(property))
      )
      Success(this.townPlan)
    }
  }

}
