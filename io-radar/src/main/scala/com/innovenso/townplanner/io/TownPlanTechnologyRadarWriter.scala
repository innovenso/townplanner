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
      outputContext: OutputContext
  ): OutputContext = {
    val workingDirectory: File =
      Files.createTempDirectory("TownplannerRadar").toFile

    outputContext.withOutputs(
      townPlan.technologyRadars
        .map(Radar(_))
        .flatMap(radar =>
          TechnologyRadarJsonWriter
            .write(workingDirectory, radar)
            .map(TechnologyRadarSiteWriter.write(_, radar, assetRepository))
            .getOrElse(Nil)
        )
    )
  }
}
