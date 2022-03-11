package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.{OutputContext, Success}
import com.innovenso.townplan.repository.FileSystemAssetRepository
import com.innovenso.townplanner.io.model.Radar
import com.innovenso.townplanner.model.concepts.views.TechnologyRadar
import com.innovenso.townplanner.model.language.{CompiledView, View}
import com.innovenso.townplanner.model.meta.Key
import com.innovenso.townplanner.model.samples.SampleFactory
import com.innovenso.townplanner.model.{EnterpriseArchitecture, TownPlan}

import java.io.File
import java.nio.file.Files
import scala.io.Source

trait RadarIO {
  val ea = new EnterpriseArchitecture()
  val targetDirectory: File =
    Files.createTempDirectory("TownplannerRadar").toFile
  val assetDirectory = new File(targetDirectory, "assets")
  val assetRepository = new FileSystemAssetRepository(assetDirectory.toPath)
  val samples: SampleFactory = SampleFactory(ea)

  val townPlanTechnologyRadarWriter: TownPlanTechnologyRadarWriter =
    TownPlanTechnologyRadarWriter(assetRepository)

  def jsonIsWritten(technologyRadar: TechnologyRadar): Boolean =
    townPlan
      .technologyRadar(technologyRadar.key)
      .exists(view =>
        TechnologyRadarJsonWriter
          .write(targetDirectory, Radar(view))
          .map(file => {
            println(Source.fromFile(file).getLines.mkString)
            file.canRead
          })
          .getOrElse(false)
      )

  def siteIsGenerated: Boolean = TownPlanTechnologyRadarWriter(assetRepository)
    .write(townPlan, OutputContext(Nil))
    .outputs
    .size == 1

  def townPlan: TownPlan = ea.townPlan

}
