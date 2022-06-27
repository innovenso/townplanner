package com.innovenso.townplanner.io.latex.model

import org.apache.commons.io.FileUtils

import java.io.{File, InputStream}

trait LatexLibrary {
  def name: String
  def resourceBasePathName: Option[String]
  def files: List[LatexLibraryFile]

  def write(targetDirectory: File): Unit = {
    files.foreach(_.write(resourceBasePathName, targetDirectory))
  }

}

case class LatexLibraryFile(
    targetFileName: Option[String] = None,
    resourcePath: String
) {
  def write(
      resourceBasePathName: Option[String],
      targetDirectory: File
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
    val targetFile: File =
      new File(targetDirectory, targetFileName.getOrElse(resourcePath))

    println(targetFile.getAbsolutePath)
    inputStream.foreach(FileUtils.copyInputStreamToFile(_, targetFile))

  }
}
