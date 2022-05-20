package com.innovenso.townplanner.io

import cats.effect.IO
import com.innovenso.townplan.io.context.{
  Failure,
  Html,
  Output,
  OutputContext,
  Pdf,
  Png,
  Success,
  Svg,
  Xml
}
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.context.{Diagram, WebPage}
import com.innovenso.townplanner.io.model.{
  HtmlSpecification,
  IndexPage,
  NavigationContext,
  NavigationPages,
  NavigationTree,
  StaticAssetSpecification,
  StyleOrImageAssetSpecification
}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.Technology
import com.innovenso.townplanner.model.concepts.views.SingleElementView
import com.innovenso.townplanner.model.language.Element
import org.apache.commons.io.FileUtils
import com.innovenso.townplanner.model.meta.{Layer, Today}
import website.html.{ElementPage, Index, TechRadar}

import java.io.{File, InputStream}
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.UUID

case class TownPlanWebsiteWriter()(implicit assetRepository: AssetRepository) {
  def write()(implicit
      townPlan: TownPlan,
      outputContext: OutputContext
  ): OutputContext =
    outputContext.withOutputs(
      workingDirectory
        .map(dir => {
          staticFiles()
          stylesAndImages()
          navigation()
          pages()
        })
        .getOrElse(Nil)
    )

  def workingDirectory: Option[File] = Some(
    Files.createTempDirectory("TownplannerWebsite").toFile
  )
  val targetDirectory: File =
    Files.createTempDirectory("TownplannerWebsiteOutput").toFile

  def staticFiles()(implicit
      outputContext: OutputContext,
      townPlan: TownPlan
  ): Unit = {
    outputContext.outputsOfFileType(Png).foreach(copyStaticFile)
    outputContext.outputsOfFileType(Svg).foreach(copyStaticFile)
    outputContext.outputsOfFileType(Pdf).foreach(copyStaticFile)
    outputContext.outputsOfFileType(Xml).foreach(copyStaticFile)
  }

  def stylesAndImages(): Unit = {
    copyClassPathResource("/pico.min.css", "pico.min.css")
    copyClassPathResource("/townplanner.css", "townplanner.css")
    copyClassPathResource("/logo.svg", "logo.svg")
    copyClassPathResource("/d3.v4.min.js", "d3.v4.min.js")
    copyClassPathResource("/radar-0.6.js", "radar-0.6.js")
  }

  private def copyClassPathResource(
      path: String,
      targetFileName: String
  ): Unit = {
    val inputStream: InputStream = getClass.getResourceAsStream(path)
    val targetFile: File = File.createTempFile("Style", "Asset")

    FileUtils.copyInputStreamToFile(inputStream, targetFile)
    val specification =
      StyleOrImageAssetSpecification(targetFile, targetFileName)
    assetRepository.write(specification.file, specification.assetName)
  }

  private def copyStaticFile(output: Output): Unit = output.assetName
    .flatMap(assetRepository.read)
    .map(file => StaticAssetSpecification(file, output))
    .foreach(specification =>
      assetRepository.write(specification.file, specification.assetName)
    )

  def pages()(implicit
      outputContext: OutputContext,
      townPlan: TownPlan
  ): List[Output] = {
    townPlan
      .components(classOf[Element])
      .map(element =>
        HtmlSpecification(
          element,
          navigationContext =>
            ElementPage(
              element,
              navigationContext,
              townPlan,
              outputContext
            ).body
        )
      )
      .map(writeHtml)
  }

  def navigation()(implicit
      outputContext: OutputContext,
      townPlan: TownPlan
  ): Unit = {
    NavigationPages(townPlan).pages.foreach(page => {
      val outputFile =
        File.createTempFile(UUID.randomUUID.toString, Html.extension)

      val html = page match {
        case technologyPage: IndexPage
            if technologyPage.modelComponentType
              .exists(mct =>
                mct.value == "Technology"
              ) && townPlan.technologyRadars.nonEmpty => {
          TechRadar(page, townPlan).body
        }
        case _ => Index(page, townPlan).body
      }
      FileUtils.writeStringToFile(outputFile, html, StandardCharsets.UTF_8)
      assetRepository.write(outputFile, page.assetName)
    })
  }

  def writeHtml(htmlSpecification: HtmlSpecification): Output = {
    val outputFile =
      File.createTempFile(UUID.randomUUID.toString, Html.extension)

    try {
      FileUtils.writeStringToFile(
        outputFile,
        htmlSpecification.html,
        StandardCharsets.UTF_8
      )
      assetRepository.write(outputFile, htmlSpecification.assetName)
      Output(
        view = SingleElementView(htmlSpecification.element),
        relatedModelComponents = List(htmlSpecification.element),
        result = Success,
        assetName = Some(htmlSpecification.assetName),
        fileType = Html,
        outputType = WebPage,
        day = Today
      )
    } catch {
      case error: Throwable =>
        Output(
          view = SingleElementView(htmlSpecification.element),
          result = Failure(error.getMessage),
          assetName = None,
          fileType = Html,
          outputType = WebPage,
          day = Today
        )
    }

  }

}
