package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.language.{HasViews, View}
import com.innovenso.townplanner.model.meta.{Key, SortKey}

case class CurrentState(
    sortKey: SortKey,
    title: String,
    description: String,
    illustratedByView: Option[Key]
) extends Context

object CurrentState {
  def apply(
      title: String = "Current State",
      description: String,
      illustratedBy: Option[View] = None
  ): CurrentState = new CurrentState(
    sortKey = SortKey.next,
    title = title,
    description = description,
    illustratedByView = illustratedBy.map(_.key)
  )
}
case class Goal(
    sortKey: SortKey,
    title: String,
    description: String,
    illustratedByView: Option[Key]
) extends Context

object Goal {
  def apply(
      title: String = "Goal",
      description: String,
      illustratedBy: Option[View] = None
  ): Goal = new Goal(
    sortKey = SortKey.next,
    title = title,
    description = description,
    illustratedByView = illustratedBy.map(_.key)
  )
}

case class Assumption(
    sortKey: SortKey,
    title: String,
    description: String,
    illustratedByView: Option[Key]
) extends Context

object Assumption {
  def apply(
      title: String = "Assumption",
      description: String,
      illustratedBy: Option[View] = None
  ): Assumption = new Assumption(
    sortKey = SortKey.next,
    title = title,
    description = description,
    illustratedByView = illustratedBy.map(_.key)
  )
}

case class Solution(
    sortKey: SortKey,
    forProblemOrRequirement: Option[String],
    title: String,
    description: String,
    illustratedByView: Option[Key]
) extends Context

object Solution {
  def apply(
      title: String = "Solution",
      forProblemOrRequirement: Option[String] = None,
      description: String,
      illustratedBy: Option[View] = None
  ): Solution = new Solution(
    sortKey = SortKey.next,
    title = title,
    description = description,
    forProblemOrRequirement = forProblemOrRequirement,
    illustratedByView = illustratedBy.map(_.key)
  )
}

case class CounterMeasure(
    sortKey: SortKey,
    title: String,
    against: String,
    description: String,
    illustratedByView: Option[Key]
) extends Context

object CounterMeasure {
  def apply(
      title: String = "Counter Measure",
      description: String,
      against: String,
      illustratedBy: Option[View] = None
  ): CounterMeasure = new CounterMeasure(
    sortKey = SortKey.next,
    title = title,
    against = against,
    description = description,
    illustratedByView = illustratedBy.map(_.key)
  )
}

case class Consequence(
    sortKey: SortKey,
    title: String,
    description: String,
    illustratedByView: Option[Key]
) extends Context

object Consequence {
  def apply(
      title: String = "Consequence",
      description: String,
      illustratedBy: Option[View] = None
  ): Consequence = new Consequence(
    sortKey = SortKey.next,
    title = title,
    description = description,
    illustratedByView = illustratedBy.map(_.key)
  )
}

trait Context extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = true
  def title: String
  def description: String
  def illustratedByView: Option[Key]
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
