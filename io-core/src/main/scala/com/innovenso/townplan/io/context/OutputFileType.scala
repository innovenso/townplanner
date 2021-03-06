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

case object Html extends OutputFileType {
  val extension = ".html"
  val name = "HTML"
}

case object Xml extends OutputFileType {
  val extension = ".xml"
  val name = "XML"
}

case object MarkDown extends OutputFileType {
  val extension = ".md"
  val name = "MarkDown"
}
