package com.innovenso.townplanner.model.meta

import org.apache.commons.text.WordUtils

import java.util.UUID

case class Key(value: String) {
  require(! Key.regex.r.matches(value), "key should be lower case with only underscores: "+ value)
  def camelCased: String = WordUtils.capitalizeFully(value, '-', '_').replaceAll("-", "").replaceAll("_", "").replaceAll("0", "Zero").replaceAll("1", "One").replaceAll("2", "Two").replaceAll("3", "Three").replaceAll("4", "Four").replaceAll("5", "Five").replaceAll("6", "Six").replaceAll("7", "Seven").replaceAll("8", "Eight").replaceAll("9", "Nine")
}

object Key {
  val regex = "[^A-Za-z0-9]"
  def apply(value: String): Key = new Key(value.replaceAll(regex, "_").toLowerCase())
  def apply(): Key = apply(UUID.randomUUID().toString)
}
