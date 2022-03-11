package com.innovenso.townplanner.io.model

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.{
  Framework,
  Language,
  Platform,
  Technique,
  Technology,
  Tool
}
import com.innovenso.townplanner.model.concepts.properties.{
  BeEliminated,
  BeInvestedIn,
  BeMigrated,
  BeTolerated
}
import com.innovenso.townplanner.model.concepts.views.{
  CompiledTechnologyRadar,
  TechnologyRadar
}
import com.innovenso.townplanner.model.language.TimelessView
import com.innovenso.townplanner.model.meta.{
  Key,
  Layer,
  ModelComponentType,
  SortKey,
  TechnologyLayer
}

case class Radar(
    title: String = "Technology Radar",
    blips: List[Blip] = Nil
) extends TimelessView {
  val key: Key = Key()
  val sortKey: SortKey = SortKey.next
  val layer: Layer = TechnologyLayer
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Technology Radar"
  )

  val quadrants =
    List(
      Radar.tools,
      Radar.techniques,
      Radar.languagesAndFrameworks,
      Radar.platforms
    )
  val rings = List(
    BeInvestedIn().name,
    BeTolerated().name,
    BeMigrated().name,
    BeEliminated().name
  )
}

object Radar {
  val tools = "Tools"
  val techniques = "Techniques"
  val languagesAndFrameworks = "Languages & Frameworks"
  val platforms = "Platforms"

  def apply(technologyRadar: CompiledTechnologyRadar) = new Radar(
    blips = technologyRadar.technologies.map(Blip(_))
  )
}

case class Blip(
    name: String,
    quadrant: String,
    ring: String,
    isNew: Boolean,
    description: String
)

object Blip {
  def apply(technology: Technology): Blip = technology match {
    case tool: Tool           => apply(tool)
    case technique: Technique => apply(technique)
    case language: Language   => apply(language)
    case framework: Framework => apply(framework)
    case platform: Platform   => apply(platform)
  }
  def apply(tool: Tool) = new Blip(
    tool.title,
    Radar.tools,
    tool.architectureVerdict.name,
    false,
    description(tool)
  )
  def apply(technique: Technique) = new Blip(
    technique.title,
    Radar.techniques,
    technique.architectureVerdict.name,
    false,
    description(technique)
  )
  def apply(language: Language) = new Blip(
    language.title,
    Radar.languagesAndFrameworks,
    language.architectureVerdict.name,
    false,
    description(language)
  )
  def apply(framework: Framework) = new Blip(
    framework.title,
    Radar.languagesAndFrameworks,
    framework.architectureVerdict.name,
    false,
    description(framework)
  )
  def apply(platform: Platform) = new Blip(
    platform.title,
    Radar.platforms,
    platform.architectureVerdict.name,
    false,
    description(platform)
  )

  def description(technology: Technology): String = technology.descriptions
    .map(v => s"${v.value}")
    .mkString(
      "<br/>"
    ) + "<br/>" + technology.architectureVerdict.descriptionOption
    .map(v => s"<b>$v</b><br/>")
    .getOrElse("") + technology.links
    .map(l => s"<a href=\"${l.url}\">${l.name}</a>")
    .mkString("<br/>")
}
