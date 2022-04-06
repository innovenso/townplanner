package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context._
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.context.Diagram
import com.innovenso.townplanner.io.model.DiagramSpecification
import net.sourceforge.plantuml.{
  FileFormat,
  FileFormatOption,
  SourceStringReader
}

import java.io.{BufferedWriter, File, FileOutputStream, FileWriter, PrintWriter}
import java.nio.file.Files
import java.util.UUID

object DiagramImageWriter {
  def diagrams(
      specification: DiagramSpecification,
      assetRepository: AssetRepository
  ): List[Output] = {
    List((FileFormat.SVG, Svg), (FileFormat.EPS, Eps), (FileFormat.PNG, Png))
      .map(ff => diagram(specification, assetRepository, ff._1, ff._2))
  }

  private def diagram(
      specification: DiagramSpecification,
      assetRepository: AssetRepository,
      fileFormat: FileFormat,
      outputFileType: OutputFileType
  ): Output = {
    val outputFile =
      File.createTempFile(UUID.randomUUID.toString, fileFormat.getFileSuffix)

    val specificationReader = new SourceStringReader(
      specification.plantumlSpecification
    )
    val outputStream = new FileOutputStream(outputFile)
    try {
      specificationReader.outputImage(
        outputStream,
        new FileFormatOption(fileFormat)
      )
      val assetName = specification.assetName(outputFileType)
      assetRepository.write(outputFile, assetName)
      Output(
        view = specification.view.view,
        result = Success,
        assetName = Some(assetName),
        fileType = outputFileType,
        outputType = Diagram,
        day = specification.view.pointInTime
      )
    } catch {
      case error: Throwable =>
        Output(
          view = specification.view.view,
          result = Failure(error.getMessage),
          assetName = None,
          fileType = outputFileType,
          outputType = Diagram,
          day = specification.view.pointInTime
        )
    } finally if (outputStream != null) outputStream.close()
  }
}
