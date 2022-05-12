package com.innovenso.townplanner.io.model

import com.innovenso.townplan.io.context.Output

import java.io.File

case class StyleOrImageAssetSpecification(file: File, filename: String) {
  val assetName: String = "website/static/" + filename
}
