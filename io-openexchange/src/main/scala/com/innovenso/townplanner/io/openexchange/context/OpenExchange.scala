package com.innovenso.townplanner.io.openexchange.context

import com.innovenso.townplan.io.context.OutputType

case object OpenExchange extends OutputType {
  val title: String = "OpenExchange"
  val description: String =
    "An export in OpenExchange XML, which can be imported in tools that support this specification"

}
