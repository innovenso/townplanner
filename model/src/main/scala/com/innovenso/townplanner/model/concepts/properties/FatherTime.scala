package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{
  ADay,
  InTheFuture,
  InThePast,
  Key,
  SortKey,
  Today
}

trait FatherTime extends Property {
  val key: Key = Key()
  def canBePlural: Boolean
  def name: String
  def description: String
  def date: ADay
  def withDate(newDate: ADay): FatherTime
  def sortKey: SortKey = SortKey(
    Some(s"${date.year}_${date.month}_${date.day}")
  )

  def appears: Boolean = false
  def fadesIn: Boolean = false
  def fadesOut: Boolean = false
  def disappears: Boolean = false

  def isBetween(
      day1: ADay,
      day2: ADay
  ): Boolean = {
    val first =
      if (day1.isBefore(day2)) day1 else day2
    val last =
      if (day1.isAfterOrEqual(day2)) day1
      else day2
    isAfterOrEqual(first) && isBefore(last)
  }

  def isBefore(day: ADay): Boolean =
    date.isBefore(day)

  def isBeforeOrEqual(day: ADay): Boolean = date.isBeforeOrEqual(day)

  def isAfterOrEqual(day: ADay): Boolean =
    date.isAfterOrEqual(day)
}

case class Conceived(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  override val fadesIn: Boolean = true
  val name = "Conceived"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)
}

case class Due(date: ADay = Today, description: String = "")
    extends FatherTime {
  val name = "Due"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)
}

case class Started(date: ADay = Today, description: String = "")
    extends FatherTime {
  override def appears: Boolean = true
  val name = "Started"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)
}

case class Done(date: ADay = Today, description: String = "")
    extends FatherTime {
  val name = "Done"
  val canBePlural = false

  override def disappears: Boolean = true

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)
}

case class StartedDevelopment(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  override val fadesIn: Boolean = true
  val name = "In Development"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

}

case class GoneToPreproduction(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  override val fadesIn: Boolean = true
  val name = "In Preproduction"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

}

case class GoneToProduction(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  override val appears: Boolean = true
  val name = "In Production"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)
}

case class Active(date: ADay = Today, description: String = "")
    extends FatherTime {
  override val appears: Boolean = true
  val name = "Active"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)
}

case class Retired(date: ADay = Today, description: String = "")
    extends FatherTime {
  override val fadesOut: Boolean = true
  val name = "Retired"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

}

case class Decommissioned(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  override val disappears: Boolean = true
  val name = "Decommissioned"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)
}

case class LifecycleEvent(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  val name = "Life Event"
  val canBePlural = true

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)
}

trait HasFatherTime extends HasProperties {
  def dueDate: Option[FatherTime] =
    lifeEvents.find(_.isInstanceOf[Due]).orElse(Some(Started(InTheFuture)))
  def doneDate: Option[FatherTime] =
    lifeEvents.find(_.isInstanceOf[Done]).orElse(Some(Started(InTheFuture)))
  def startDate: Option[FatherTime] =
    lifeEvents.find(_.isInstanceOf[Started]).orElse(Some(Started(InThePast)))

  def isUnknownLifecycle(day: ADay): Boolean =
    lifeEvents.isEmpty || (hasNoLifeEventsBefore(
      day
    ) && !hasAppearedAfter(day) && hasDisappearedAfter(day))

  def isDecommissioned(day: ADay): Boolean =
    hasDisappearedBefore(day)

  def isPhasingOut(day: ADay): Boolean =
    hasFadedOutBefore(day) && !hasDisappearedBefore(day)

  def isActive(day: ADay): Boolean = hasAppearedBeforeOrOn(
    day
  ) && !isPhasingOut(day) && !isDecommissioned(day)

  def isPlanned(day: ADay): Boolean =
    hasFadedInBefore(day) && !isActive(day) && !isPhasingOut(
      day
    ) && !isDecommissioned(day)

  def isNotEvenPlanned(day: ADay): Boolean =
    !isPlanned(day) && !isActive(day) && !isDecommissioned(
      day
    ) && !isDecommissioned(day) && !isUnknownLifecycle(day)

  private def hasFadedInBefore(day: ADay): Boolean =
    lifeEvents.filter(_.isBefore(day)).exists(_.fadesIn)

  private def hasFadedInAfter(day: ADay): Boolean =
    lifeEvents.filter(_.isAfterOrEqual(day)).exists(_.fadesIn)

  private def hasAppearedBeforeOrOn(
      day: ADay
  ): Boolean =
    lifeEvents
      .filter(_.isBeforeOrEqual(day))
      .exists(_.appears)

  private def hasAppearedAfter(
      day: ADay
  ): Boolean =
    lifeEvents
      .filter(_.isAfterOrEqual(day))
      .exists(_.appears)

  private def hasFadedOutBefore(day: ADay): Boolean =
    lifeEvents.filter(_.isBefore(day)).exists(_.fadesOut)

  private def hasFadedOutAfter(day: ADay): Boolean =
    lifeEvents.filter(_.isAfterOrEqual(day)).exists(_.fadesOut)

  def lifeEvents: List[FatherTime] = props(classOf[FatherTime])

  private def hasDisappearedBefore(day: ADay): Boolean =
    lifeEvents.filter(_.isBefore(day)).exists(_.disappears)

  private def hasDisappearedAfter(day: ADay): Boolean =
    lifeEvents.filter(_.isAfterOrEqual(day)).exists(_.disappears)

  private def hasNoLifeEventsBefore(day: ADay): Boolean =
    !lifeEvents.exists(_.isBefore(day))

  private def hasNoLifeEventsAfter(day: ADay): Boolean =
    !lifeEvents.exists(_.isAfterOrEqual(day))
}

case class FatherTimeConfigurer(
    modelComponent: HasFatherTime,
    fatherTime: FatherTime,
    propertyAdder: CanAddProperties
) {
  def on(day: ADay): HasFatherTime =
    propertyAdder.withProperty(
      modelComponent,
      fatherTime.withDate(newDate = day)
    )
}

trait CanConfigureFatherTime[ModelComponentType <: HasFatherTime] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(fatherTime: FatherTime): FatherTimeConfigurer =
    FatherTimeConfigurer(modelComponent, fatherTime, propertyAdder)

  def is(fatherTime: FatherTime): FatherTimeConfigurer =
    FatherTimeConfigurer(modelComponent, fatherTime, propertyAdder)
}
