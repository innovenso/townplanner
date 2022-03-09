package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.{
  Failure,
  Html,
  Output,
  OutputFileType,
  Success
}
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.context.RadarOutput
import com.innovenso.townplanner.io.model.Radar
import org.apache.commons.lang3.SystemUtils

import java.io.File
import java.util.Optional
import java.util.concurrent.Executors
import scala.tools.nsc.io.Directory

object TechnologyRadarSiteWriter {
  def write(
      jsonFile: File,
      radar: Radar,
      assetRepository: AssetRepository
  ): List[Output] = {
    val workingDirectory = jsonFile.getParentFile
    val targetDirectory = new File(workingDirectory, "dist")
    val process = new ProcessBuilder()
      .command(
        npx,
        "tech-radar-generator",
        jsonFile.getName,
        "./dist"
      )
      .directory(workingDirectory)
      .start
    val exitCode = process.waitFor
    if (exitCode != 0) {
      List(
        Output(
          radar,
          Failure("Radar Generator Failed"),
          None,
          Html,
          RadarOutput,
          radar.pointInTime
        )
      )
    } else {
      siteFiles(targetDirectory).foreach(file => {
        println(file.getAbsolutePath)
        assetRepository.write(file, assetName(radar, targetDirectory, file))
        println(
          assetRepository
            .read(assetName(radar, targetDirectory, file))
            .get
            .getAbsolutePath
        )
      })
      List(
        Output(
          radar,
          Success,
          Some(
            assetName(
              radar,
              targetDirectory,
              new File(targetDirectory, "index.html")
            )
          ),
          Html,
          RadarOutput,
          radar.pointInTime
        )
      )
    }

  }

  private def assetName(
      radar: Radar,
      distDirectory: File,
      file: File
  ): String = {
    val path = radar.layer.name + "/Technology Radar/" + distDirectory.toPath
      .relativize(file.toPath)
      .toString
    println(path)
    path
  }

  private val npx = if (SystemUtils.IS_OS_WINDOWS) "npx.cmd" else "npx"

  def siteFiles(dir: File): Array[File] = {
    val filesList = dir.listFiles
    filesList
      .filter(_.isFile) ++ filesList.filter(_.isDirectory).flatMap(siteFiles)
  }
}
