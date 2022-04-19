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

  private def prepareDependencies(): Unit = {
    imagesDirectory.mkdirs()
    specification.dependencies
      .foreach(output =>
        output.assetName
          .flatMap(assetRepository.read)
          .foreach(file =>
            FileUtils
              .copyFile(file, new File(imagesDirectory, output.assetHashedName))
          )
      )
  }

  private def prepareLibraries(): Unit = {
    specification.latexLibraries.foreach(_.write(workingDirectory))
  }

  def document: List[Output] = {
    prepareLibraries()
    prepareDependencies()
    sourceCodeFile
      .map(pdf)
      .toOption
      .toList
  }

  private def sourceCodeFile: Try[File] =
    Try {
      val outputFile = new File(workingDirectory, "document.tex")
      val fileWriter = new FileWriter(outputFile)
      fileWriter.write(specification.latexSourceCode)
      fileWriter.close()
      println(outputFile.getAbsolutePath)
      outputFile
    }

  private def pdf(
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
        view = specification.view.view,
        result = Failure("LaTeX Compilation Failed"),
        assetName = None,
        fileType = Pdf,
        outputType = specification.outputType,
        day = specification.view.pointInTime
      )
    } else {
      assetRepository.write(
        new File(sourceCodeFile.getParentFile, "document.pdf"),
        specification.assetName
      )
      Output(
        view = specification.view.view,
        relatedModelComponents = specification.relatedModelComponents,
        result = Success,
        assetName = Some(specification.assetName),
        fileType = Pdf,
        outputType = specification.outputType,
        day = specification.view.pointInTime
      )
    }
  }

  private val latexmk = "latexmk"

}

private class OutputLogger(stream: InputStream) extends Runnable {
  override def run(): Unit = new BufferedReader(
    new InputStreamReader(stream)
  ).lines.forEach(it => println(it))
}
