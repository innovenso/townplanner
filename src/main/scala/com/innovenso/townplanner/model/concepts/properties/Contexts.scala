package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey, Title}

import scala.util.Try

case class CurrentState(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title = Title("Current State"),
    description: Description
) extends Context
case class Goal(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title = Title("Goal"),
    description: Description
) extends Context
case class Assumption(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title = Title("Assumption"),
    description: Description
) extends Context
case class Consequence(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title = Title("Assumption"),
    description: Description
) extends Context

trait Context extends Property {
  val canBePlural: Boolean = true
  def title: Title
  def description: Description
}

trait HasContext extends HasProperties {
  def currentState: List[CurrentState] = props(classOf[CurrentState])
  def goals: List[Goal] = props(classOf[Goal])
  def assumptions: List[Assumption] = props(classOf[Assumption])
  def consequences: List[Consequence] = props(classOf[Consequence])
}

trait CanAddContext extends CanAddProperties {
  def withContext[ModelComponentType <: HasContext](
      modelComponent: ModelComponentType,
      context: Context
  ): Try[(TownPlan, ModelComponentType)] =
    withProperty(modelComponent, context)
}
