package com.innovenso.townplan.io.context

import com.innovenso.townplanner.model.language.View
import com.innovenso.townplanner.model.meta.ADay
import org.apache.commons.codec.digest.DigestUtils

case class Output(
    view: View,
    result: OutputStatus,
    assetName: Option[String],
    fileType: OutputFileType,
    outputType: OutputType,
    day: ADay
) {
  def assetHashedName: String =
    assetName.map(DigestUtils.sha1Hex).map(_ + fileType.extension).getOrElse("")
}
