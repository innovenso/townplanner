package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAddRelationships,
  HasRelationships,
  Serving
}
import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.language._
import com.innovenso.townplanner.model.meta._

case class BusinessCapabilityMap(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    forEnterprise: Key,
    title: String = "Business Capability Map",
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends TimelessView {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Business Capability Map"
  )
  val layer: Layer = StrategyLayer

  def withProperty(property: Property): BusinessCapabilityMap =
    copy(properties = this.properties + (property.key -> property))

}

object BusinessCapabilityMap {
  def apply(
      forEnterprise: Enterprise
  ) = new BusinessCapabilityMap(
    forEnterprise = forEnterprise.key
  )
}

trait HasBusinessCapabilityMaps
    extends HasViews
    with HasBusinessCapabilities
    with HasEnterprises
    with HasBusinessActors with HasTags {
  def businessCapabilityMaps: List[CompiledBusinessCapabilityMap] = components(
    classOf[BusinessCapabilityMap]
  ).map(view => BusinessCapabilityMapCompiler(view, this).compile)
  def businessCapabilityMap(key: Key): Option[CompiledBusinessCapabilityMap] =
    component(key, classOf[BusinessCapabilityMap]).map(
      BusinessCapabilityMapCompiler(_, this).compile
    )

}

trait CanAddBusinessCapabilityMaps
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      businessCapabilityMap: BusinessCapabilityMap
  ): BusinessCapabilityMap =
    has(businessCapabilityMap)
}

case class CompiledBusinessCapabilityMap(
    view: BusinessCapabilityMap,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[BusinessCapabilityMap]
    with HasRelationships
    with HasBusinessCapabilities
    with HasEnterprises
    with HasTags {
  def enterprise: Option[Enterprise] = enterprises.headOption
  def level0BusinessCapabilities: List[BusinessCapability] =
    enterprise.map(level0businessCapabilities).getOrElse(Nil)
  def firstTag(businessCapability: BusinessCapability): Option[Tag] = tags(businessCapability).headOption
}

case class BusinessCapabilityMapCompiler(
    view: BusinessCapabilityMap,
    source: HasBusinessCapabilityMaps
) extends ViewCompiler[
      BusinessCapabilityMap,
      CompiledBusinessCapabilityMap,
      HasBusinessCapabilities
    ] {
  def compile: CompiledBusinessCapabilityMap =
    CompiledBusinessCapabilityMap(
      view,
      viewTitle,
      groupTitle(view.forEnterprise),
      viewComponents(
        enterprise.toList ++ capabilities ++ servingCapabilities ++ servingEnterprises ++ tags
      )
    )

  private def enterprise: Option[Enterprise] =
    source.enterprise(view.forEnterprise)

  private def capabilities: List[BusinessCapability] =
    enterprise.map(source.businessCapabilityMap).getOrElse(Nil)

  private def tags: List[Tag] = capabilities.flatMap(source.tags(_)).distinct

  private def servingCapabilities: List[Serving] = capabilities
    .flatMap(c =>
      source.relationships(c, classOf[Serving], classOf[BusinessCapability])
    )
    .map(_.asInstanceOf[Serving])

  private def servingEnterprises: List[Serving] = capabilities
    .flatMap(c =>
      source.relationships(c, classOf[Serving], classOf[Enterprise])
    )
    .map(_.asInstanceOf[Serving])
}
