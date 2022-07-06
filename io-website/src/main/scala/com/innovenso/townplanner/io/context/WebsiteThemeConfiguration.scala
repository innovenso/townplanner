package com.innovenso.townplanner.io.context

object WebsiteThemeConfiguration {
  private var _themeLibrary: HtmlLibrary = DefaultWebsiteTheme

  def configureTheme(newTheme: HtmlLibrary): Unit =
    this._themeLibrary = newTheme

  def theme: HtmlLibrary = _themeLibrary
}
