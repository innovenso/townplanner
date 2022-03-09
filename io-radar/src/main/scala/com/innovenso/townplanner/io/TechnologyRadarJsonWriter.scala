package com.innovenso.townplanner.io
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.innovenso.townplanner.io.model.Radar
import com.innovenso.townplanner.model.TownPlan
import net.liftweb
import net.liftweb.json
import net.liftweb.json.JsonAST.RenderSettings.pretty

import java.io.{File, FileWriter}
import java.util.UUID
import scala.util.Try

object TechnologyRadarJsonWriter {
  def write(workingDirectory: File, radar: Radar): Try[File] = Try {
    val outputFile = new File(workingDirectory, "radar.json")
    val fileWriter = new FileWriter(outputFile)
    fileWriter.write(prettyRender(jsonRadar(radar)))
    fileWriter.close()
    outputFile
  }

  def jsonRadar(radar: Radar): json.JObject =
    ("title" -> radar.title) ~
      ("quadrants" -> radar.quadrants) ~ ("rings" -> radar.rings) ~
      getBlips(radar)

  def getBlips(radar: Radar): (String, List[liftweb.json.JObject]) =
    ("blips" ->
      radar.blips.map(b =>
        ("name" -> b.name) ~ ("quadrant" -> b.quadrant) ~ ("ring" -> b.ring) ~ ("isNew" -> b.isNew) ~ ("description" -> b.description)
      ))
}
