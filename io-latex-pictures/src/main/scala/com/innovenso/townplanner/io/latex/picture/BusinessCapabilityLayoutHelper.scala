package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplan.io.model.{ColorScheme, IOConfiguration}
import com.innovenso.townplanner.io.latex.model.{
  AboveOf,
  BelowOf,
  Fill,
  Fit,
  LeftOf,
  RightOf,
  TikzStyleInstruction
}
import com.innovenso.townplanner.model.concepts.BusinessCapability
import com.innovenso.townplanner.model.concepts.views.CompiledBusinessCapabilityMap

case class BusinessCapabilityLayoutHelper(
    view: CompiledBusinessCapabilityMap
) {

  private def positionOfCapabilityInLevel(
      businessCapability: BusinessCapability
  ): Int =
    if (view.level(businessCapability) == 0)
      view.level0BusinessCapabilities.indexOf(businessCapability)
    else
      view
        .parentBusinessCapability(businessCapability)
        .map(view.childBusinessCapabilities(_).indexOf(businessCapability))
        .getOrElse(-1)

  def positionInstruction(
      businessCapability: BusinessCapability
  ): Option[TikzStyleInstruction] = (
    view.level(businessCapability),
    positionOfCapabilityInLevel(businessCapability)
  ) match {
    case (0, 0) => None
    case (0, _) =>
      capabilityPreceding(businessCapability).map(b =>
        RightOf(b.key.camelCased + ".south east")
      )
    case (1, 0) =>
      view
        .parentBusinessCapability(businessCapability)
        .map(b => BelowOf(b.key.camelCased, Some(2.5)))
    case (1, _) =>
      lastOfPreviousCapabilityChildren(businessCapability).map(b =>
        BelowOf(b.key.camelCased, Some(5))
      )
    case (2, 0) =>
      view
        .parentBusinessCapability(businessCapability)
        .map(b => BelowOf(b.key.camelCased))
    case (2, _) =>
      capabilityPreceding(businessCapability).map(b =>
        BelowOf(b.key.camelCased)
      )
    case _ => None
  }

  def backgroundColorInstruction(
      businessCapability: BusinessCapability
  ): Option[TikzStyleInstruction] = {
    val index = view.level0BusinessCapabilities.indexOf(businessCapability)
    val colorScheme: ColorScheme = IOConfiguration.colorScheme
    if (index < 0) None
    else {
      val accentNumber = index % colorScheme.numberOfAccentColors + 1
      Some(Fill(s"innovensoAccent" + accentNumber))
    }
  }

  def boxFitInstruction(
      businessCapability: BusinessCapability
  ): TikzStyleInstruction = Fit(
    nodesToFitInBox(businessCapability) ::: coordinatesToFitInBox(
      businessCapability
    )
  )

  private def nodesToFitInBox(
      businessCapability: BusinessCapability
  ): List[String] =
    capabilitiesToFitInBox(businessCapability).map(_.key.camelCased)

  private def coordinatesToFitInBox(
      businessCapability: BusinessCapability
  ): List[String] = view.level(businessCapability) match {
    case 0 =>
      view.level0BusinessCapabilities.map(b =>
        s"${businessCapability.key.camelCased}.north|-${b.key.camelCased}.north"
      ) ::: view.level0BusinessCapabilities
        .flatMap(lastChild)
        .map(b =>
          s"${businessCapability.key.camelCased}.south|-${b.key.camelCased}.south"
        )
    case _ => Nil
  }

  def lastChild(
      businessCapability: BusinessCapability
  ): Option[BusinessCapability] = view.level(businessCapability) match {
    case 0 =>
      view
        .childBusinessCapabilities(businessCapability)
        .lastOption
        .flatMap(lastChild)
    case 1 =>
      view
        .childBusinessCapabilities(businessCapability)
        .lastOption
        .orElse(Some(businessCapability))
    case _ => None
  }

  private def capabilitiesToFitInBox(
      businessCapability: BusinessCapability
  ): List[BusinessCapability] = view.level(businessCapability) match {
    case 0 =>
      List(businessCapability) ::: view
        .childBusinessCapabilities(businessCapability)
        .lastOption
        .flatMap(view.childBusinessCapabilities(_).lastOption)
        .toList
    case 1 =>
      List(businessCapability) ::: view
        .childBusinessCapabilities(businessCapability)
        .lastOption
        .toList
    case _ => Nil
  }

  private def capabilityPreceding(
      businessCapability: BusinessCapability
  ): Option[BusinessCapability] = {
    val containingList = view
      .parentBusinessCapability(businessCapability)
      .map(view.childBusinessCapabilities)
      .getOrElse(view.level0BusinessCapabilities)
    val index = containingList.indexOf(businessCapability)
    if (index > 0) Some(containingList(index - 1)) else None
  }

  private def lastOfPreviousCapabilityChildren(
      businessCapability: BusinessCapability
  ): Option[BusinessCapability] = {
    println(s"last of previous capability's children for ${businessCapability}")
    val result = capabilityPreceding(businessCapability)
      .flatMap(it => view.childBusinessCapabilities(it).lastOption)
      .orElse(capabilityPreceding(businessCapability))
    println(result)
    result
  }
}
