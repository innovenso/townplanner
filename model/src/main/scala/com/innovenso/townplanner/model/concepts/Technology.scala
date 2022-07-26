package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

sealed trait Technology
    extends Element
    with HasDescription
    with HasLinks
    with HasSWOT
    with HasArchitectureVerdict
    with CanBeImpacted
    with CanImplement
    with CanBeKnown
    with HasTagProperties {
  val modelComponentType: ModelComponentType = ModelComponentType("Technology")
  val aspect: Aspect = PassiveStructure
  val layer: Layer = TechnologyLayer
  val sortKey: SortKey = SortKey.next
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

trait LanguageOrFramework extends Technology {
  val technologyType: String = "Languages and Frameworks"
}

case class Language(
    key: Key = Key(),
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends LanguageOrFramework {
  def withProperty(property: Property): Language =
    copy(properties = this.properties + (property.key -> property))
}

case class Framework(
    key: Key = Key(),
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends LanguageOrFramework {
  def withProperty(property: Property): Framework =
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

trait CanBeImplementedByTechnologies extends CanBeImplemented

trait HasTechnologies extends HasModelComponents with HasRelationships {
  def technologies: List[Technology] = components(classOf[Technology])
  def technology(key: Key): Option[Technology] =
    component(key, classOf[Technology])
  def technologies[TechnologyClass <: Technology](
      element: CanBeImplementedByTechnologies,
      technologyClass: Class[TechnologyClass]
  ): List[TechnologyClass] =
    directIncomingDependencies(
      element,
      classOf[Implementation],
      technologyClass
    )
  def technologies(element: CanBeImplementedByTechnologies): List[Technology] =
    technologies(element, classOf[Technology])
  def languages(element: CanBeImplementedByTechnologies): List[Language] =
    technologies(element, classOf[Language])
  def frameworks(element: CanBeImplementedByTechnologies): List[Framework] =
    technologies(element, classOf[Framework])
  def tools(element: CanBeImplementedByTechnologies): List[Tool] =
    technologies(element, classOf[Tool])
  def techniques(element: CanBeImplementedByTechnologies): List[Technique] =
    technologies(element, classOf[Technique])
  def platforms(element: CanBeImplementedByTechnologies): List[Platform] =
    technologies(element, classOf[Platform])

  def technologyLabel(
      element: CanBeImplementedByTechnologies
  ): Option[String] = {
    val tech =
      languages(element) ::: frameworks(element) ::: techniques(
        element
      ) ::: platforms(element)
    if (tech.isEmpty) None
    else Some(tech.map(_.title).mkString(","))
  }

  def peopleKnowingTechnology(technology: Technology): List[Person] =
    directIncomingDependencies(technology, classOf[Knowledge], classOf[Person])
  def teamsKnowingTechnology(technology: Technology): List[Team] =
    peopleKnowingTechnology(technology)
      .flatMap(person =>
        directIncomingDependencies(person, classOf[Composition], classOf[Team])
      )
      .distinct
  def technologiesKnownBy(element: CanKnow): List[Technology] =
    directOutgoingDependencies(element, classOf[Knowledge], classOf[Technology])
  def modelComponentsImplementedByTechnology[
      ElementType <: CanBeImplementedByTechnologies
  ](
      technology: Technology,
      elementClass: Class[ElementType]
  ): List[ElementType] =
    directOutgoingDependencies(
      technology,
      classOf[Implementation],
      elementClass
    )
  def technologies[TechnologyType <: Technology](
      technologyClass: Class[TechnologyType]
  ): List[TechnologyType] = technologies
    .filter(technologyClass.isInstance(_))
    .map(technologyClass.cast(_))
  def technologies[TechnologyType <: Technology](
      technologyClass: Class[TechnologyType],
      verdictClass: Class[_ <: ArchitectureVerdict]
  ): List[TechnologyType] = technologies(technologyClass).filter(tech =>
    verdictClass.isInstance(tech.architectureVerdict)
  )

}

case class TechnologyRadarConfigurer[TechnologyType <: Technology](
    modelComponent: TechnologyType,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[Technology]
    with CanConfigureLinks[Technology]
    with CanConfigureSWOT[Technology]
    with CanConfigureArchitectureVerdict[Technology]
    with CanConfigureTagProperties[Technology]
    with CanConfigureImplementationSource[Technology]
    with CanConfigureKnowledgeTarget[Technology] {
  def as(
      body: TechnologyRadarConfigurer[TechnologyType] => Any
  ): TechnologyType = {
    body.apply(this)
    propertyAdder.townPlan
      .component(modelComponent.key, modelComponent.getClass)
      .get
  }
}

trait CanAddTechnologies extends CanAddProperties with CanAddRelationships {
  def describes[TechnologyType <: Technology](
      technology: TechnologyType
  ): TechnologyRadarConfigurer[TechnologyType] =
    TechnologyRadarConfigurer(has(technology), this, this)
}
