package com.innovenso.townplan.io.illustration

import com.innovenso.townplanner.model.concepts.properties.ImageIllustration
import com.innovenso.townplanner.model.meta.Key
import org.apache.commons.io.FileUtils

import java.io.{File, InputStream}
import java.nio.file.{Files, Path}
import javax.imageio.ImageIO
import scala.util.Try

case class ImageIllustrationFile(illustration: ImageIllustration) {
  private val relativeResourceUrl: String = illustration.imagePath
  private val absoluteResourceUrl = s"/$relativeResourceUrl"
  private def inputStream: Option[InputStream] = {
    Option(getClass.getResourceAsStream(absoluteResourceUrl))
      .orElse(
        Option(ClassLoader.getSystemResourceAsStream(relativeResourceUrl))
      )
      .orElse(
        Option(FileUtils.openInputStream(new File(relativeResourceUrl)))
      )
  }
  private val temporaryFile: Option[File] = Try {
    val targetDirectory: File = Files.createTempDirectory("image").toFile
    new File(targetDirectory, s"${Key().value}.png")
  }.toOption

  def load: Option[File] = temporaryFile
    .map(f => {
      inputStream.foreach(is =>
        Option(ImageIO.read(is))
          .foreach(bi => Try { ImageIO.write(bi, "PNG", f) })
      )
      f
    })

}
