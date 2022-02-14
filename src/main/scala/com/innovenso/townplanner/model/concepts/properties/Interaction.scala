package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.language.Element
import com.innovenso.townplanner.model.meta.{Key, SortKey}

abstract class Interaction extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = true
  val sortKey: SortKey = SortKey.next
  def name: String
  def description: String
  def source: Key
  def target: Key

  def withSource(newSource: Key): Interaction
  def withTarget(newTarget: Key): Interaction
}

case class Message(
    name: String,
    description: String = "",
    source: Key = Key(),
    target: Key = Key()
) extends Interaction {
  override def withSource(newSource: Key): Interaction =
    copy(source = newSource)

  override def withTarget(newTarget: Key): Interaction =
    copy(target = newTarget)
}

case class Request(
    name: String,
    description: String = "",
    source: Key = Key(),
    target: Key = Key()
) extends Interaction {
  override def withSource(newSource: Key): Interaction =
    copy(source = newSource)

  override def withTarget(newTarget: Key): Interaction =
    copy(target = newTarget)

}

case class Response(
    name: String,
    description: String = "",
    source: Key = Key(),
    target: Key = Key()
) extends Interaction {
  override def withSource(newSource: Key): Interaction =
    copy(source = newSource)

  override def withTarget(newTarget: Key): Interaction =
    copy(target = newTarget)

}

trait HasInteractions extends HasProperties {
  def interactions: List[Interaction] = props(classOf[Interaction])
  def interaction(key: Key): Option[Interaction] =
    prop(key, classOf[Interaction])
  def withInteraction(interaction: Interaction): HasProperties =
    withProperty(interaction)
}

case class InteractionConfigurer(
    modelComponent: HasInteractions,
    interaction: Interaction,
    propertyAdder: CanAddProperties
) {
  def from(source: Element): InteractionConfigurer =
    copy(interaction = interaction.withSource(source.key))
  def to(target: Element): HasInteractions =
    propertyAdder.withProperty(
      modelComponent,
      interaction.withTarget(target.key)
    )
}

trait CanConfigureInteractions[ModelComponentType <: HasInteractions] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(interaction: Interaction): InteractionConfigurer =
    InteractionConfigurer(modelComponent, interaction, propertyAdder)
}
