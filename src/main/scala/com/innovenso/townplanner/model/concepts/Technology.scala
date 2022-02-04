package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  HasArchitectureVerdict,
  HasDocumentation,
  Property
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

case class Technology(
    key: Key,
    sortKey: SortKey,
    title: Title,
    description: Description,
    technologyType: TechnologyType,
    properties: Map[Key, Property]
) extends Element
    with HasDocumentation
    with HasArchitectureVerdict
    with CanImplement {
  val modelComponentType: ModelComponentType = ModelComponentType("Technology")
  val aspect: Aspect = PassiveStructure
  val layer: Layer = TechnologyLayer

  def withProperty(property: Property): Technology =
    copy(properties = this.properties + (property.key -> property))
}

trait HasTechnologies extends HasModelComponents {
  def technologies: List[Technology] = components(classOf[Technology])
  def technology(key: Key): Option[Technology] =
    component(key, classOf[Technology])
}

sealed trait TechnologyType {
  def name: String
}

case object Techniques extends TechnologyType {
  val name = "Techniques"
}

case object LanguageAndFramework extends TechnologyType {
  val name = "Languages and Frameworks"
}

case object Platforms extends TechnologyType {
  val name = "Platforms"
}

case object Tools extends TechnologyType {
  val name = "Tools"
}
