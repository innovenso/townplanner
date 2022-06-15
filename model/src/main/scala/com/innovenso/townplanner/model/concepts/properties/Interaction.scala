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
  def technology: Option[String]
  def payload: Option[String]

  def withSource(newSource: Key): Interaction
  def withTarget(newTarget: Key): Interaction
  def withPayload(payload: String): Interaction
  def withTechnology(technology: String): Interaction

  def label(counter: Int = -1): String =
    s"${labelCounter(counter)}${name}${labelTechnologyAndPayload}"

  private def labelCounter(counter: Int): String =
    if (counter < 0) "" else s"${counter}. "
  private def labelTechnologyAndPayload: String = {
    val contents = technology.toList ::: payload.toList
    if (contents.nonEmpty) s" [${contents.mkString(",")}]" else ""
  }
}

case class Message(
    name: String,
    description: String = "",
    source: Key = Key(),
    target: Key = Key(),
    technology: Option[String] = None,
    payload: Option[String] = None
) extends Interaction {
  override def withSource(newSource: Key): Interaction =
    copy(source = newSource)

  override def withTarget(newTarget: Key): Interaction =
    copy(target = newTarget)

  override def withPayload(payload: String): Interaction =
    copy(payload = Option(payload))

  override def withTechnology(technology: String): Interaction =
    copy(technology = Option(technology))
}

case class Request(
    name: String,
    description: String = "",
    source: Key = Key(),
    target: Key = Key(),
    technology: Option[String] = None,
    payload: Option[String] = None
) extends Interaction {
  override def withSource(newSource: Key): Interaction =
    copy(source = newSource)

  override def withTarget(newTarget: Key): Interaction =
    copy(target = newTarget)
  override def withPayload(payload: String): Interaction =
    copy(payload = Option(payload))

  override def withTechnology(technology: String): Interaction =
    copy(technology = Option(technology))

}

case class Response(
    name: String,
    description: String = "",
    source: Key = Key(),
    target: Key = Key(),
    technology: Option[String] = None,
    payload: Option[String] = None
) extends Interaction {
  override def withSource(newSource: Key): Interaction =
    copy(source = newSource)

  override def withTarget(newTarget: Key): Interaction =
    copy(target = newTarget)
  override def withPayload(payload: String): Interaction =
    copy(payload = Option(payload))

  override def withTechnology(technology: String): Interaction =
    copy(technology = Option(technology))

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
  def containing(payload: String): InteractionConfigurer =
    copy(interaction = interaction.withPayload(payload))

  def using(technology: String): InteractionConfigurer =
    copy(interaction = interaction.withTechnology(technology))

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
