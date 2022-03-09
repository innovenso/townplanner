package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.model.Radar
import com.innovenso.townplanner.model.TownPlan

import java.io.File
import java.nio.file.{Files, Path}

case class TownPlanTechnologyRadarWriter(
    assetRepository: AssetRepository
) {
  def write(
      townPlan: TownPlan,
      outputContext: OutputContext,
      title: Option[String]
  ): OutputContext = {
    val workingDirectory: File =
      Files.createTempDirectory("TownplannerRadar").toFile

    val radar: Radar =
      if (title.isDefined) Radar(townPlan, title.get) else Radar(townPlan)

    outputContext.withOutputs(
      TechnologyRadarJsonWriter
        .write(workingDirectory, radar)
        .map(TechnologyRadarSiteWriter.write(_, radar, assetRepository))
        .getOrElse(Nil)
    )
  }
}
