package com.innovenso.townplan.io.context

trait OutputFileType {
  def extension: String
  def name: String
}

object Pdf extends OutputFileType {
  val extension = ".pdf"
  val name = "PDF"
}

object Eps extends OutputFileType {
  val extension = ".eps"
  val name = "Embedded Postscript"
}

object Svg extends OutputFileType {
  val extension = ".svg"
  val name = "Scalable Vector Graphics"
}

object Png extends OutputFileType {
  val extension = ".png"
  val name = "Portable Network Graphics"
}
