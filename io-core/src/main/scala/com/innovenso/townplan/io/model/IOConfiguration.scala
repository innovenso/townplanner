package com.innovenso.townplan.io.model

object IOConfiguration {
  private var _colorScheme: ColorScheme = ColorScheme()

  def configureColorScheme(newColorScheme: ColorScheme): Unit =
    this._colorScheme = newColorScheme

  def colorScheme: ColorScheme = _colorScheme
}
