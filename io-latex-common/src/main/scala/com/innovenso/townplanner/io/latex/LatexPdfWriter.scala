package com.innovenso.townplanner.io.latex

import com.innovenso.townplan.io.context.{
  Eps,
  Failure,
  Html,
  Output,
  OutputContext,
  OutputType,
  Pdf,
  Success
}
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.latex.model.LatexSpecification
import com.innovenso.townplanner.model.language.View
import org.apache.commons.io.FileUtils

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

case class LatexPdfWriter(
    specification: LatexSpecification,
    assetRepository: AssetRepository,
    outputContext: OutputContext
) {
  val workingDirectory: File =
    Files.createTempDirectory("TownplannerLatex").toFile
  val imagesDirectory: File = new File(workingDirectory, "images")

  def prepareDependencies(): Unit = {
    imagesDirectory.mkdirs()
    outputContext
      .outputsOfFileType(Eps)
      .foreach(output =>
        output.assetName
          .flatMap(assetRepository.read)
          .foreach(file =>
            FileUtils
              .copyFile(file, new File(imagesDirectory, output.assetHashedName))
          )
      )
  }

  def document: List[Output] = {
    prepareDependencies()
    sourceCodeFile
      .map(pdf)
      .toOption
      .toList
  }

  def sourceCodeFile: Try[File] =
    Try {
      val outputFile = new File(workingDirectory, "document.tex")
      val fileWriter = new FileWriter(outputFile)
      fileWriter.write(specification.latexSourceCode)
      fileWriter.close()
      println(outputFile.getAbsolutePath)
      outputFile
    }

  def pdf(
      sourceCodeFile: File
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
    val outputLogger = new OutputLogger(process.getInputStream)
    Executors.newSingleThreadExecutor.submit(outputLogger)
    val exitCode = process.waitFor
    if (exitCode != 0) {
      Output(
        specification.view.view,
        Failure("LaTeX Compilation Failed"),
        None,
        Pdf,
        specification.outputType,
        specification.view.pointInTime
      )
    } else {
      assetRepository.write(
        new File(sourceCodeFile.getParentFile, "document.pdf"),
        specification.assetName
      )
      Output(
        specification.view.view,
        Success,
        Some(specification.assetName),
        Pdf,
        specification.outputType,
        specification.view.pointInTime
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
