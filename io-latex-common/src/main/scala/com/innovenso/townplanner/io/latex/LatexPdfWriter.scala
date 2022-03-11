package com.innovenso.townplanner.io.latex

import com.innovenso.townplan.io.context.{
  Failure,
  Html,
  Output,
  OutputType,
  Pdf,
  Success
}
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.model.language.View
import org.apache.commons.lang3.SystemUtils

import java.io.{
  BufferedReader,
  File,
  FileWriter,
  InputStream,
  InputStreamReader
}
import java.nio.file.Files
import java.util.concurrent.Executors
import scala.util.Try

object LatexPdfWriter {
  def document(
      sourceCode: String,
      view: View,
      outputType: OutputType,
      assetName: String,
      assetRepository: AssetRepository
  ): List[Output] = {
    val workingDirectory = Files.createTempDirectory("TownplannerLatex").toFile
    sourceCodeFile(sourceCode, workingDirectory)
      .map(pdf(_, view, outputType, assetName, assetRepository))
      .toOption
      .toList
  }

  def sourceCodeFile(sourceCode: String, workingDirectory: File): Try[File] =
    Try {
      val outputFile = new File(workingDirectory, "document.tex")
      val fileWriter = new FileWriter(outputFile)
      fileWriter.write(sourceCode)
      fileWriter.close()
      println(outputFile.getAbsolutePath)
      outputFile
    }

  def pdf(
      sourceCodeFile: File,
      view: View,
      outputType: OutputType,
      assetName: String,
      assetRepository: AssetRepository
  ): Output = {
    println("writing PDF file")
    val process = new ProcessBuilder()
      .command(
        latexmk,
        "-pdf",
        "-interaction=nonstopmode",
        "document.tex"
      )
      .directory(sourceCodeFile.getParentFile)
      .start
    val streamGobbler = new OutputLogger(process.getInputStream)
    Executors.newSingleThreadExecutor.submit(streamGobbler)
    val exitCode = process.waitFor
    if (exitCode != 0) {
      Output(
        view,
        Failure("LaTeX Compilation Failed"),
        None,
        Pdf,
        outputType,
        view.pointInTime
      )
    } else {
      assetRepository.write(
        new File(sourceCodeFile.getParentFile, "document.pdf"),
        assetName
      )
      Output(
        view,
        Success,
        Some(assetName),
        Pdf,
        outputType,
        view.pointInTime
      )
    }
  }

  private val latexmk = "latexmk"

}

private class OutputLogger(stream: InputStream) extends Runnable {
  override def run(): Unit = new BufferedReader(
    new InputStreamReader(stream)
  ).lines.forEach(println(_))
}
