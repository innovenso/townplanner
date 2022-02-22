package com.innovenso.townplan.io.context

import com.innovenso.townplanner.model.language.View
import com.innovenso.townplanner.model.meta.ADay

case class Output(
    view: View,
    result: OutputStatus,
    assetName: Option[String],
    fileType: OutputFileType,
    outputType: OutputType,
    day: ADay
) {}
