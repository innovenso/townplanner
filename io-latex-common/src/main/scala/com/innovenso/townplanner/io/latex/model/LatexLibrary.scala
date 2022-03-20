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
    var resourceUrl =
      "/" + resourceBasePathName.map(p => p + "/").getOrElse("") + resourcePath
    println(resourceUrl)
    val inputStream: InputStream = getClass.getResourceAsStream(resourceUrl)
    val targetFile: File =
      new File(targetDirectory, targetFileName.getOrElse(resourcePath))

    println(targetFile.getAbsolutePath)
    FileUtils.copyInputStreamToFile(inputStream, targetFile)
  }
}

case object KaoBookLibrary extends LatexLibrary {
  val name = "kaobook"
  val resourceBasePathName: Option[String] = Some("latex/templates/kaobook")
  val files: List[LatexLibraryFile] = List(
    "kao.sty",
    "kaobiblio.sty",
    "kaobook.cls",
    "kaorefs.sty",
    "kaotheorems.sty"
  ).map(name => LatexLibraryFile(resourcePath = name))
}
