package com.innovenso.townplan.io.context

trait OutputFileType {
  def extension: String
  def name: String
}

case object Pdf extends OutputFileType {
  val extension = ".pdf"
  val name = "PDF"
}

case object Eps extends OutputFileType {
  val extension = ".eps"
  val name = "Embedded Postscript"
}

case object Svg extends OutputFileType {
  val extension = ".svg"
  val name = "Scalable Vector Graphics"
}

case object Png extends OutputFileType {
  val extension = ".png"
  val name = "Portable Network Graphics"
}

case object PlantUML extends OutputFileType {
  val extension = ".puml"
  val name = "PlantUML Specification"
}
