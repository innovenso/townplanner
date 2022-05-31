package com.innovenso.townplanner.io.latex.slides.model

import com.innovenso.townplanner.model.meta.Today

case object DocumentVersion {
  val version: String = s"${Today.year}.${Today.month}.${Today.day}"
}
