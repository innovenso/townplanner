package com.innovenso.townplanner.io.context

import com.innovenso.townplan.io.context.OutputType

case object WebPage extends OutputType {
  val title: String = "Web Page"
  val description: String =
    "An static Web page"
}

case object WebAsset extends OutputType {
  val title: String = "Web Asset"
  val description: String = "An asset in a website, being an image, css, javascript, ..."
}

