package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  HasDocumentation,
  Property
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._
import com.innovenso.townplanner.model.{CanManipulateTownPlan, TownPlan}

import scala.util.Try

case class Enterprise(
    key: Key,
    sortKey: SortKey,
    title: Title,
    description: Description,
    properties: Map[Key, Property]
) extends Element
    with HasDocumentation
    with CanCompose
    with CanBeComposedOf
    with CanBeServed {
  val layer: Layer = BusinessLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType("Enterprise")

  def withProperty(property: Property): Enterprise =
    copy(properties = this.properties + (property.key -> property))
}

trait HasEnterprises extends HasModelComponents {
  def enterprises: List[Enterprise] = components(classOf[Enterprise])
  def enterprise(key: Key): Option[Enterprise] =
    component(key, classOf[Enterprise])
}

trait CanAddEnterprises extends CanManipulateTownPlan {
  def withEnterprise(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None)
  ): Try[(TownPlan, Enterprise)] = withNewModelComponent(
    Enterprise(
      key = key,
      sortKey = sortKey,
      title = title,
      description = description,
      Map.empty[Key, Property]
    )
  )
}
