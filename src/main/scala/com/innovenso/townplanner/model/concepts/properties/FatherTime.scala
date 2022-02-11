package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.concepts.KeyPointInTime
import com.innovenso.townplanner.model.meta.{ADay, Key, SortKey, Today}

import java.time.LocalDate

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

  def isBefore(pointInTime: KeyPointInTime): Boolean =
    date.isBefore(pointInTime.date)
  def isAfterOrEqual(pointInTime: KeyPointInTime): Boolean =
    date.isAfterOrEqual(pointInTime.date)
  def isBetween(
      pointInTime1: KeyPointInTime,
      pointInTime2: KeyPointInTime
  ): Boolean = {
    val first =
      if (pointInTime1.isBefore(pointInTime2)) pointInTime1 else pointInTime2
    val last =
      if (pointInTime1.isAfterOrEqual(pointInTime2)) pointInTime1
      else pointInTime2
    isAfterOrEqual(first) && isBefore(last)
  }
}

case class Conceived(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  val name = "Conceived"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

  override val fadesIn: Boolean = true
}

case class Due(date: ADay = Today, description: String = "")
    extends FatherTime {
  val name = "Due"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

}

case class StartedDevelopment(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  val name = "In Development"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

  override val fadesIn: Boolean = true

}

case class GoneToPreproduction(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  val name = "In Preproduction"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

  override val fadesIn: Boolean = true

}

case class GoneToProduction(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  val name = "In Production"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

  override val appears: Boolean = true
}

case class Active(date: ADay = Today, description: String = "")
    extends FatherTime {
  val name = "Active"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

  override val appears: Boolean = true
}

case class Retired(date: ADay = Today, description: String = "")
    extends FatherTime {
  val name = "Retired"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

  override val fadesOut: Boolean = true

}

case class Decommissioned(
    date: ADay = Today,
    description: String = ""
) extends FatherTime {
  val name = "Decommissioned"
  val canBePlural = false

  override def withDate(newDate: ADay): FatherTime = copy(date = newDate)

  override val disappears: Boolean = true
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
  def lifeEvents: List[FatherTime] = props(classOf[FatherTime])
  def isUnknownLifecycle(pointInTime: KeyPointInTime): Boolean =
    lifeEvents.isEmpty || (hasNoLifeEventsBefore(
      pointInTime
    ) && !hasAppearedAfter(pointInTime) && hasDisappearedAfter(pointInTime))
  def isDecommissioned(pointInTime: KeyPointInTime): Boolean =
    hasDisappearedBefore(pointInTime)
  def isPhasingOut(pointInTime: KeyPointInTime): Boolean =
    hasFadedOutBefore(pointInTime) && !hasDisappearedBefore(pointInTime)
  def isActive(pointInTime: KeyPointInTime): Boolean = hasAppearedBefore(
    pointInTime
  ) && !isPhasingOut(pointInTime) && !isDecommissioned(pointInTime)
  def isPlanned(pointInTime: KeyPointInTime): Boolean =
    hasFadedInBefore(pointInTime) && !isActive(pointInTime) && !isPhasingOut(
      pointInTime
    ) && !isDecommissioned(pointInTime)
  def isNotEvenPlanned(pointInTime: KeyPointInTime): Boolean =
    !isPlanned(pointInTime) && !isActive(pointInTime) && !isDecommissioned(
      pointInTime
    ) && !isDecommissioned(pointInTime) && !isUnknownLifecycle(pointInTime)

  private def hasFadedInBefore(pointInTime: KeyPointInTime): Boolean =
    lifeEvents.filter(_.isBefore(pointInTime)).exists(_.fadesIn)
  private def hasFadedInAfter(pointInTime: KeyPointInTime): Boolean =
    lifeEvents.filter(_.isAfterOrEqual(pointInTime)).exists(_.fadesIn)
  private def hasAppearedBefore(
      pointInTime: KeyPointInTime
  ): Boolean =
    lifeEvents
      .filter(_.isBefore(pointInTime))
      .exists(_.appears)
  private def hasAppearedAfter(
      pointInTime: KeyPointInTime
  ): Boolean =
    lifeEvents
      .filter(_.isAfterOrEqual(pointInTime))
      .exists(_.appears)
  private def hasFadedOutBefore(pointInTime: KeyPointInTime): Boolean =
    lifeEvents.filter(_.isBefore(pointInTime)).exists(_.fadesOut)
  private def hasFadedOutAfter(pointInTime: KeyPointInTime): Boolean =
    lifeEvents.filter(_.isAfterOrEqual(pointInTime)).exists(_.fadesOut)
  private def hasDisappearedBefore(pointInTime: KeyPointInTime): Boolean =
    lifeEvents.filter(_.isBefore(pointInTime)).exists(_.disappears)
  private def hasDisappearedAfter(pointInTime: KeyPointInTime): Boolean =
    lifeEvents.filter(_.isAfterOrEqual(pointInTime)).exists(_.disappears)
  private def hasNoLifeEventsBefore(pointInTime: KeyPointInTime): Boolean =
    !lifeEvents.exists(_.isBefore(pointInTime))
  private def hasNoLifeEventsAfter(pointInTime: KeyPointInTime): Boolean =
    !lifeEvents.exists(_.isAfterOrEqual(pointInTime))
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
