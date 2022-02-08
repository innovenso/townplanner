package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

case class CurrentState(
    sortKey: SortKey = SortKey(None),
    title: String = "Current State",
    description: String
) extends Context
case class Goal(
    sortKey: SortKey = SortKey(None),
    title: String = "Goal",
    description: String
) extends Context
case class Assumption(
    sortKey: SortKey = SortKey(None),
    title: String = "Assumption",
    description: String
) extends Context
case class Consequence(
    sortKey: SortKey = SortKey(None),
    title: String = "Assumption",
    description: String
) extends Context

trait Context extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = true
  def title: String
  def description: String
}

trait HasContext extends HasProperties {
  def currentState: List[CurrentState] = props(classOf[CurrentState])
  def goals: List[Goal] = props(classOf[Goal])
  def assumptions: List[Assumption] = props(classOf[Assumption])
  def consequences: List[Consequence] = props(classOf[Consequence])
}

trait CanConfigureContext[
    ModelComponentType <: HasContext
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(context: Context): ModelComponentType =
    propertyAdder.withProperty(modelComponent, context)
}
