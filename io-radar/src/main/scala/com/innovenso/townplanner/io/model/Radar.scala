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
    blips =
      technologyRadar.technologies.map(tech => Blip(tech, technologyRadar))
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
  def apply(
      technology: Technology,
      technologyRadar: CompiledTechnologyRadar
  ): Blip = technology match {
    case tool: Tool           => apply(tool, technologyRadar)
    case technique: Technique => apply(technique, technologyRadar)
    case language: Language   => apply(language, technologyRadar)
    case framework: Framework => apply(framework, technologyRadar)
    case platform: Platform   => apply(platform, technologyRadar)
  }
  def apply(tool: Tool, technologyRadar: CompiledTechnologyRadar) = new Blip(
    tool.title,
    Radar.tools,
    tool.architectureVerdict.name,
    false,
    description(tool, technologyRadar)
  )
  def apply(technique: Technique, technologyRadar: CompiledTechnologyRadar) =
    new Blip(
      technique.title,
      Radar.techniques,
      technique.architectureVerdict.name,
      false,
      description(technique, technologyRadar)
    )
  def apply(language: Language, technologyRadar: CompiledTechnologyRadar) =
    new Blip(
      language.title,
      Radar.languagesAndFrameworks,
      language.architectureVerdict.name,
      false,
      description(language, technologyRadar)
    )
  def apply(framework: Framework, technologyRadar: CompiledTechnologyRadar) =
    new Blip(
      framework.title,
      Radar.languagesAndFrameworks,
      framework.architectureVerdict.name,
      false,
      description(framework, technologyRadar)
    )
  def apply(platform: Platform, technologyRadar: CompiledTechnologyRadar) =
    new Blip(
      platform.title,
      Radar.platforms,
      platform.architectureVerdict.name,
      false,
      description(platform, technologyRadar)
    )

  def description(
      technology: Technology,
      technologyRadar: CompiledTechnologyRadar
  ): String = technology.descriptions
    .map(v => s"${v.value}")
    .mkString(
      "<br/>"
    ) + "<br/>" + technology.architectureVerdict.descriptionOption
    .map(v => s"<b>$v</b><br/>")
    .getOrElse("") + technology.links
    .map(l => s"<a href=\"${l.url}\">${l.name}</a>")
    .mkString("<br/>") + "<br/>Used in IT Systems: " + technologyRadar
    .systemsImplementedWith(technology)
    .map(s => s"<i>${s.title}</i>")
    .mkString(", ") + "<br/>Used in IT Container: " + technologyRadar
    .containersImplementedWith(technology)
    .map(s => s"<i>${s.title}</i>")
    .mkString(", ") + "<br/>Known by: " + technologyRadar
    .businessActorsWithKnowledgeOf(technology)
    .map(s => s"<i>${s.title}</i>")
    .mkString(", ")
}
