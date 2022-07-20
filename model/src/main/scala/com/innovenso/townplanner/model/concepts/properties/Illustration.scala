package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.concepts.views.FlowView
import com.innovenso.townplanner.model.meta.{Key, SortKey}

trait Illustration extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = true
  def title: Option[String]
  def description: Option[String]
}

case class FlowViewIllustration(
    sortKey: SortKey,
    title: Option[String],
    description: Option[String],
    flowViewKey: Key
) extends Illustration

object FlowViewIllustration {
  def apply(
      title: Option[String] = None,
      description: Option[String] = None,
      flowView: FlowView
  ): FlowViewIllustration = new FlowViewIllustration(
    sortKey = SortKey.next,
    title = title,
    description = description,
    flowViewKey = flowView.key
  )
}

trait CanBeIllustrated extends HasProperties {
  def flowViewIllustrations: List[FlowViewIllustration] = props(
    classOf[FlowViewIllustration]
  )
}

trait CanConfigureIllustrations[
    ModelComponentType <: CanBeIllustrated
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def isIllustratedBy(illustration: Illustration): ModelComponentType =
    propertyAdder.withProperty(modelComponent, illustration)
}
