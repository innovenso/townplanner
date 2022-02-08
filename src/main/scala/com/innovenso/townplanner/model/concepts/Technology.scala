package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAddRelationships,
  CanConfigureImplementationSource,
  CanImplement
}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

sealed trait Technology
    extends Element
    with HasDescription
    with HasArchitectureVerdict
    with CanImplement {
  val modelComponentType: ModelComponentType = ModelComponentType("Technology")
  val aspect: Aspect = PassiveStructure
  val layer: Layer = TechnologyLayer
  val sortKey: SortKey = SortKey(None)
  def technologyType: String

}

case class Technique(
    key: Key = Key(),
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Technology {
  val technologyType: String = "Techniques"

  def withProperty(property: Property): Technique =
    copy(properties = this.properties + (property.key -> property))
}

case class LanguageOrFramework(
    key: Key = Key(),
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Technology {
  val technologyType: String = "Languages and Frameworks"

  def withProperty(property: Property): LanguageOrFramework =
    copy(properties = this.properties + (property.key -> property))
}

case class Platform(
    key: Key = Key(),
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Technology {
  val technologyType: String = "Platforms"

  def withProperty(property: Property): Platform =
    copy(properties = this.properties + (property.key -> property))
}

case class Tool(
    key: Key = Key(),
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Technology {
  val technologyType: String = "Tools"

  def withProperty(property: Property): Tool =
    copy(properties = this.properties + (property.key -> property))
}

case class TechnologyRadar(
    tools: List[Tool],
    techniques: List[Technique],
    languagesAndFrameworks: List[LanguageOrFramework],
    platforms: List[Platform]
)

trait HasTechnologies extends HasModelComponents {
  def technologies: List[Technology] = components(classOf[Technology])
  def technology(key: Key): Option[Technology] =
    component(key, classOf[Technology])
  def technologyRadar: TechnologyRadar = TechnologyRadar(
    tools = components(classOf[Tool]),
    techniques = components(classOf[Technique]),
    languagesAndFrameworks = components(classOf[LanguageOrFramework]),
    platforms = components(classOf[Platform])
  )
}

case class TechnologyRadarConfigurer(
    modelComponent: Technology,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[Technology]
    with CanConfigureArchitectureVerdict[Technology]
    with CanConfigureImplementationSource[Technology] {
  def as(
      body: TechnologyRadarConfigurer => Any
  ): Technology = {
    body.apply(this)
    propertyAdder.townPlan
      .technology(modelComponent.key)
      .get
  }
}

trait CanAddTechnologies extends CanAddProperties with CanAddRelationships {
  def describes(technology: Technology): TechnologyRadarConfigurer =
    TechnologyRadarConfigurer(has(technology), this, this)
}
