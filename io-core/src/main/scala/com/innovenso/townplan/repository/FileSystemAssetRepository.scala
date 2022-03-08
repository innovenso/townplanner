package com.innovenso.townplan.repository

import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.file.Path
import scala.util.Try

case class FileSystemAssetRepository(targetBasePath: Path)
    extends AssetRepository {

  override def write(asset: File, objectName: String): Try[String] = Try {
    val targetFile = getPath(objectName).toFile
    FileUtils.copyFile(asset, targetFile)
    targetFile.toPath.toUri.toString
  }

  def getPath(objectName: String): Path = {
    targetBasePath.resolve(objectName)
  }

  override def read(objectName: String): Option[File] = {
    val file = getPath(objectName).toFile
    if (file.canRead) Some(file) else None
  }

  override def objectNames: List[String] = getListOfFiles(targetBasePath.toFile)
    .map(_.toPath)
    .map(targetBasePath.relativize(_).toString)

  private def getListOfFiles(directory: File): List[File] = {
    if (directory.exists && directory.isDirectory)
      directory.listFiles.filter(_.isFile).toList ::: directory.listFiles
        .filter(_.isDirectory)
        .toList
        .flatMap(getListOfFiles)
    else
      Nil
  }
}
