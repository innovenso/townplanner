package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

case class CurrentState(
    sortKey: SortKey = SortKey.next,
    title: String = "Current State",
    description: String
) extends Context
case class Goal(
    sortKey: SortKey = SortKey.next,
    title: String = "Goal",
    description: String
) extends Context
case class Assumption(
    sortKey: SortKey = SortKey.next,
    title: String = "Assumption",
    description: String
) extends Context
case class Solution(
    sortKey: SortKey = SortKey.next,
    forProblemOrRequirement: Option[String] = None,
    title: String = "Solution",
    description: String
)
case class CounterMeasure(
    sortKey: SortKey = SortKey.next,
    title: String = "Counter Measure",
    against: String,
    description: String
)
case class Consequence(
    sortKey: SortKey = SortKey.next,
    title: String = "Consequence",
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
  def solutions: List[Solution] = props(classOf[Solution])
  def counterMeasures: List[CounterMeasure] = props(classOf[CounterMeasure])
}

trait CanConfigureContext[
    ModelComponentType <: HasContext
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(context: Context): ModelComponentType =
    propertyAdder.withProperty(modelComponent, context)
}
