package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

case class KPI(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    description: String
) extends Property {
  val canBePlural: Boolean = true
}

trait HasKPI extends HasProperties {
  def kpi: List[KPI] =
    props(classOf[KPI])
}

trait CanConfigureKPI[
    ModelComponentType <: HasKPI
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(kpi: KPI): ModelComponentType =
    propertyAdder.withProperty(modelComponent, kpi)
}
