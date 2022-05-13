package com.innovenso.townplanner.io.model

import com.innovenso.townplanner.model.concepts.Technology
import com.innovenso.townplanner.model.concepts.properties.UnknownArchitectureVerdict
import com.innovenso.townplanner.model.concepts.views.CompiledTechnologyRadar
import net.liftweb
import net.liftweb.json
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

case class TechnologyRadarDecorator(
    view: CompiledTechnologyRadar,
    canNavigate: CanNavigate
) {
  val jsonRadar: json.JObject =
    ("svg_id" -> "radar") ~
      ("width" -> 1450) ~
      ("height" -> 1000) ~
      ("colors" -> (("background" -> "#fff") ~ ("grid" -> "#bbb") ~ ("inactive" -> "#ddd"))) ~
      ("title" -> view.title) ~
      ("quadrants" -> view.categories.map(category =>
        ("name" -> category.name)
      )) ~
      ("rings" -> view.circles.map(circle =>
        ("name" -> circle.name) ~ ("color" -> "#000")
      )) ~
      ("print_layout" -> true) ~
      ("entries" -> view.technologies
        .filter(technology => !technology.isUnknownArchitectureVerdict)
        .map(technology =>
          ("label" -> technology.title) ~ ("quadrant" -> quadrant(
            technology
          )) ~ ("ring" -> circle(technology)) ~ ("active" -> isActive(
            technology
          )) ~ ("moved" -> 0) ~ ("link" -> link(technology))
        ))

  val render: String = prettyRender(jsonRadar)

  def quadrant(technology: Technology): Int =
    view.categories.indexWhere(category =>
      category.technologyClass.isInstance(technology)
    )
  def circle(technology: Technology): Int =
    view.circles.indexWhere(verdict =>
      technology.architectureVerdict.radarCircle == verdict.radarCircle
    )
  def isActive(technology: Technology): Boolean = view.isUsed(technology)
  def link(technology: Technology): String =
    canNavigate.relativePathTo(technology)
}
