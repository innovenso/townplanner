package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.language.{
  Element,
  HasModelComponents,
  ModelComponent
}
import com.innovenso.townplanner.model.meta.{
  ActiveStructure,
  Aspect,
  Business,
  Description,
  Key,
  Layer,
  ModelComponentType,
  PassiveStructure,
  SortKey,
  Title
}

case class Enterprise(
    key: Key,
    sortKey: SortKey,
    title: Title,
    description: Description
) extends Element {
  val layer: Layer = Business
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType("Enterprise")
}

trait HasEnterprises extends HasModelComponents {
  def enterprises: List[Enterprise] = values(classOf[Enterprise])
  def enterprise(key: Key): Option[Enterprise] = value(key, classOf[Enterprise])

  def isEnterprise(modelComponent: ModelComponent): Boolean =
    modelComponent.isInstanceOf[Enterprise]
  def asEnterprise(modelComponent: ModelComponent): Enterprise =
    modelComponent.asInstanceOf[Enterprise]
}
