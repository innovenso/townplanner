package com.innovenso.townplanner.io.context

import com.innovenso.townplan.repository.AssetRepository
import org.apache.commons.io.FileUtils

import java.io.{File, InputStream}
import java.nio.file.Files

trait HtmlLibrary {
  def name: String
  def resourceBasePathName: Option[String]
  def files: List[HtmlLibraryFile]

  def write(assetRepository: AssetRepository): Unit = {
    files.foreach(_.write(resourceBasePathName, assetRepository))
  }

}

case class HtmlLibraryFile(
    targetFileName: String,
    resourcePath: String
) {
  def write(
      resourceBasePathName: Option[String],
      assetRepository: AssetRepository
  ): Unit = {
    val relativeResourceUrl =
      resourceBasePathName.map(p => p + "/").getOrElse("") + resourcePath

    val absoluteResourceUrl = s"/$relativeResourceUrl"

    println(absoluteResourceUrl)
    val inputStream: Option[InputStream] = {
      Option(getClass.getResourceAsStream(absoluteResourceUrl))
        .orElse(
          Option(ClassLoader.getSystemResourceAsStream(relativeResourceUrl))
        )
        .orElse(
          Option(FileUtils.openInputStream(new File(relativeResourceUrl)))
        )
    }
    val targetFile: File = Files.createTempFile("html", "static").toFile

    inputStream.foreach(FileUtils.copyInputStreamToFile(_, targetFile))
    assetRepository.write(targetFile, "website/static/" + targetFileName)
  }
}
