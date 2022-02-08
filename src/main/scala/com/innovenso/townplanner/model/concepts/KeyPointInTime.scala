package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  CanConfigureDescription,
  HasDescription,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.CanAddRelationships
import com.innovenso.townplanner.model.language.{
  HasModelComponents,
  ModelComponent
}
import com.innovenso.townplanner.model.meta.{Key, ModelComponentType, SortKey}

import java.time.LocalDate

case class KeyPointInTime(
    date: LocalDate,
    title: String,
    diagramsNeeded: Boolean = true,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ModelComponent
    with HasDescription {
  val key: Key = Key(
    s"key_point_in_time_${date.getYear}_${date.getMonthValue}_${date.getDayOfMonth}"
  )
  val sortKey: SortKey = SortKey(Some(key.value))
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Key Point in Time"
  )
  def isToday: Boolean = LocalDate.now().equals(date)
  def isFuture: Boolean = LocalDate.now().isBefore(date)
  def isPast: Boolean = LocalDate.now().isAfter(date)

  def withProperty(property: Property): KeyPointInTime =
    copy(properties = this.properties + (property.key -> property))

}

trait HasKeyPointsInTime extends HasModelComponents {

  def pointInTime(key: Key): Option[KeyPointInTime] =
    component(key, classOf[KeyPointInTime])

  def pointInTime(localDate: LocalDate): Option[KeyPointInTime] =
    pointsInTime.find(_.date.equals(localDate))

  def pointsInTime: List[KeyPointInTime] = components(classOf[KeyPointInTime])
}

case class KeyPointInTimeConfigurer(
    modelComponent: KeyPointInTime,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[KeyPointInTime] {
  def as(
      body: KeyPointInTimeConfigurer => Any
  ): KeyPointInTime = {
    body.apply(this)
    propertyAdder.townPlan
      .pointInTime(modelComponent.key)
      .get
  }
}

trait CanAddKeyPointsInTime extends CanAddProperties with CanAddRelationships {
  def describes(pointInTime: KeyPointInTime): KeyPointInTimeConfigurer =
    KeyPointInTimeConfigurer(has(pointInTime), this, this)
}
