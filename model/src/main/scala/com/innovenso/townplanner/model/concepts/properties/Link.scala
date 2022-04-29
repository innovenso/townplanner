package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

abstract class Link extends Property {
  val key: Key = Key()
  val canBePlural: Boolean = true
  val sortKey: SortKey = SortKey.next
  def name: String
  def url: String
  def title: String
}

case class ApiDocumentation(url: String, title: String = "") extends Link {
  val name = "API Documentation"
}

case class FunctionalDocumentation(url: String, title: String = "")
    extends Link {
  val name = "Functional Documentation"
}

case class TechnicalDocumentation(url: String, title: String = "")
    extends Link {
  val name = "Technical Documentation"
}

case class ArchitectureDocumentation(url: String, title: String = "")
    extends Link {
  val name = "Architecture Documentation"
}

case class Website(url: String, title: String = "") extends Link {
  val name = "Website"
}

case class Wiki(url: String, title: String = "") extends Link {
  val name = "Wiki"
}

case class ProductionUrl(url: String, title: String = "") extends Link {
  val name = "Production URL"
}

case class PreProductionUrl(url: String, title: String = "") extends Link {
  val name = "Pre-Production URL"
}

case class DevelopmentUrl(url: String, title: String = "") extends Link {
  val name = "Development URL"
}

case class SourceCodeRepository(url: String, title: String = "") extends Link {
  val name = "Source Code Repository"
}

trait HasLinks extends HasProperties {
  def links: List[Link] =
    props(classOf[Link])
  def apiDocumentationLinks: List[Link] = props(classOf[ApiDocumentation])
  def functionalDocumentationLinks: List[Link] = props(
    classOf[FunctionalDocumentation]
  )
  def technicalDocumentationLinks: List[Link] = props(
    classOf[TechnicalDocumentation]
  )
  def architectureDocumentationLinks: List[Link] = props(
    classOf[ArchitectureDocumentation]
  )
  def websiteLinks: List[Link] = props(classOf[Website])
  def wikiLinks: List[Link] = props(classOf[Wiki])
  def productionLinks: List[Link] = props(classOf[ProductionUrl])
  def preProductionLinks: List[Link] = props(classOf[PreProductionUrl])
  def developmentLinks: List[Link] = props(classOf[DevelopmentUrl])
  def sourceCodeRepositoryLinks: List[Link] = props(
    classOf[SourceCodeRepository]
  )
}

trait CanConfigureLinks[
    ModelComponentType <: HasLinks
] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(link: Link): ModelComponentType =
    propertyAdder.withProperty(modelComponent, link)
}
