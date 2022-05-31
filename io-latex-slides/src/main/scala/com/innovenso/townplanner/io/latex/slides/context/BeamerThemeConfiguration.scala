package com.innovenso.townplanner.io.latex.slides.context

import com.innovenso.townplan.io.model.ColorScheme
import com.innovenso.townplan.io.model.IOConfiguration._colorScheme
import com.innovenso.townplanner.io.latex.model.LatexLibrary
import com.innovenso.townplanner.io.latex.slides.model.BeamerDefaultThemeLibrary

object BeamerThemeConfiguration {
  private var _themeLibrary: LatexLibrary = BeamerDefaultThemeLibrary

  def configureTheme(newTheme: LatexLibrary): Unit =
    this._themeLibrary = newTheme

  def theme: LatexLibrary = _themeLibrary
}
