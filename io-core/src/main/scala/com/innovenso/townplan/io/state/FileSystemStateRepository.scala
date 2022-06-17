package com.innovenso.townplan.io.state

import com.innovenso.townplan.io.context.OutputType
import com.innovenso.townplanner.model.language.CompiledView
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

import java.nio.charset.StandardCharsets
import java.nio.file.Path

case class FileSystemStateRepository(targetBasePath: Path)
    extends StateRepository {
  override def hasChanged(view: CompiledView[_], writer: String): Boolean =
    !getPath(view, writer).toFile.exists()

  override def touch(view: CompiledView[_], writer: String): Unit =
    FileUtils.writeStringToFile(
      getPath(view, writer).toFile,
      view.toString,
      StandardCharsets.UTF_8
    )

  def getPath(view: CompiledView[_], writer: String): Path = {
    targetBasePath.resolve(getHash(view, writer))
  }

  def getHash(view: CompiledView[_], writer: String): String =
    writer + " " + DigestUtils.sha256Hex(view.toString)

}
