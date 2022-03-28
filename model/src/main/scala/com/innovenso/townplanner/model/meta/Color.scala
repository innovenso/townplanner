package com.innovenso.townplanner.model.meta

import java.security.SecureRandom

case class Color(red: Int, green: Int, blue: Int)

object Color {
  private val secureRandom = new SecureRandom()
  private def randomInt: Int =
    secureRandom.nextInt(256)

  def random = new Color(randomInt, randomInt, randomInt)
}