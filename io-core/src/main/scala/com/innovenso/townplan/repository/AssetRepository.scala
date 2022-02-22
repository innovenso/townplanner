package com.innovenso.townplan.repository

import java.io.File
import scala.util.Try

trait AssetRepository {
  def write(asset: File, objectName: String): Try[String]
  def read(objectName: String): Option[File]
  def objectNames: List[String]
}
