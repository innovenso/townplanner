package com.innovenso.townplanner.model.meta

import org.apache.commons.lang3.StringUtils

import java.security.SecureRandom

case class Color(red: Int, green: Int, blue: Int)(name: String) {
  val hex: String = String.format("#%02x%02x%02x", red, green, blue)
  val tuple: (String, Color) =
    ("innovenso" + StringUtils.capitalize(name), this)
}

object Color {
  private val secureRandom = new SecureRandom()
  private def randomInt: Int =
    secureRandom.nextInt(256)

  def random = new Color(randomInt, randomInt, randomInt)(Key().camelCased)

  def apply(red: Int, green: Int, blue: Int)(
      name: String = Key().camelCased
  ): Color =
    new Color(red, green, blue)(name)
  def apply(hex: String)(name: String): Color = new Color(
    Integer.valueOf(hex.substring(1, 3), 16),
    Integer.valueOf(hex.substring(3, 5), 16),
    Integer.valueOf(hex.substring(5, 7), 16)
  )(name)
}
