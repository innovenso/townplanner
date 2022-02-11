package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

case class ResilienceMeasure(
    description: String
) extends Property {
  val key: Key = Key()
  val sortKey: SortKey = SortKey.next
  val canBePlural: Boolean = true
}

trait HasResilienceMeasures extends HasProperties {
  def resilienceMeasures: List[ResilienceMeasure] = props(
    classOf[ResilienceMeasure]
  )
  def resilienceMeasure(key: Key): Option[ResilienceMeasure] =
    prop(key, classOf[ResilienceMeasure])
  def withResilienceMeasure(
      resilienceMeasure: ResilienceMeasure
  ): HasProperties =
    withProperty(resilienceMeasure)
}

trait CanConfigureResilienceMeasures[
    ModelComponentType <: HasResilienceMeasures
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def provides(resilienceMeasure: ResilienceMeasure): HasResilienceMeasures =
    propertyAdder.withProperty(modelComponent, resilienceMeasure)
}
