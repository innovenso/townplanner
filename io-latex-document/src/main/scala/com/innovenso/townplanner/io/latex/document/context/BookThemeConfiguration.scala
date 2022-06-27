package com.innovenso.townplanner.io.latex.document.context

import com.innovenso.townplanner.io.latex.document.model.BookDefaultThemeLibrary
import com.innovenso.townplanner.io.latex.model.LatexLibrary

object BookThemeConfiguration {
  private var _themeLibrary: LatexLibrary = BookDefaultThemeLibrary

  def configureTheme(newTheme: LatexLibrary): Unit =
    this._themeLibrary = newTheme

  def theme: LatexLibrary = _themeLibrary
}
