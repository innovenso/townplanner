package com.innovenso.townplanner.io.model

import com.innovenso.townplan.io.context.Output

import java.io.File

case class StaticAssetSpecification (file: File, output: Output) {
  val assetName: String = "website/static/" + output.assetHashedName
}
