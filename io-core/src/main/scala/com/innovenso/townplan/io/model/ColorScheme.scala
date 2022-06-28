package com.innovenso.townplan.io.model

import com.innovenso.townplanner.model.meta.Color

case class ColorScheme(
    textColor: Color = Color(37, 26, 6)("text"),
    highlightColor: Color = Color(8, 136, 140)("highlight"),
    linkColor: Color = Color(140, 131, 79)("link"),
    headerColor: Color = Color(115, 81, 18)("header"),
    backgroundColor: Color = Color(222, 212, 196)("background"),
    borderColor: Color = Color(77, 77, 77)("border"),
    redColor: Color = Color(255, 0, 0)("red"),
    greenColor: Color = Color(0, 255, 0)("green"),
    amberColor: Color = Color(255, 255, 0)("amber"),
    blueColor: Color = Color(0, 0, 255)("blue"),
    blackColor: Color = Color(0, 0, 0)("black"),
    whiteColor: Color = Color(255, 255, 255)("white"),
    archimateColors: ArchimateColorScheme = ArchimateColorScheme(),
    accentColors: List[Color] = List(
      Color(140, 131, 79)(),
      Color(8, 156, 61)(),
      Color(141, 24, 245)(),
      Color(59, 121, 235)(),
      Color(150, 156, 39)(),
      Color(158, 127, 51)(),
      Color(98, 51, 235)(),
      Color(104, 158, 19)(),
      Color(235, 79, 49)(),
      Color(245, 33, 24)(),
      Color(47, 87, 158)()
    )
) {
  def accentColor(number: Int): Color =
    if (accentColors.isEmpty) highlightColor
    else
      accentColors(number % accentColors.length)

  val numberOfAccentColors: Int = 20

  def toMap: Map[String, Color] = Map(
    whiteColor.tuple,
    blackColor.tuple,
    greenColor.tuple,
    amberColor.tuple,
    redColor.tuple,
    blueColor.tuple,
    borderColor.tuple,
    backgroundColor.tuple,
    headerColor.tuple,
    textColor.tuple,
    highlightColor.tuple,
    linkColor.tuple
  ) ++ (1 to numberOfAccentColors).map(index =>
    ("innovensoAccent" + index, accentColor(index))
  ) ++ archimateColors.toMap
}

case class ArchimateColorScheme(
    applicationCollaboration: Color =
      Color("#afffff")("applicationCollaboration"),
    applicationComponent: Color = Color("#73aac0")("applicationComponent"),
    externalApplicationComponent: Color =
      Color("#afffff")("externalApplicationComponent"),
    applicationFunction: Color = Color("#ffffaf")("applicationFunction"),
    aplicationProcess: Color = Color("#ffffaf")("aplicationProcess"),
    applicationEvent: Color = Color("#ffffaf")("applicationEvent"),
    applicationInteraction: Color = Color("#ffffaf")("applicationInteraction"),
    applicationInterface: Color = Color("#afffff")("applicationInterface"),
    applicationService: Color = Color("#ffffaf")("applicationService"),
    artifact: Color = Color("#91ff91")("artifact"),
    businessActor: Color = Color("#e6ffff")("businessActor"),
    businessCollaboration: Color = Color("#e6ffff")("businessCollaboration"),
    businessEvent: Color = Color("#ffffe6")("businessEvent"),
    businessFunction: Color = Color("#ffffe6")("businessFunction"),
    businessCapability: Color = Color("#fff0e6")("businessCapability"),
    businessInteraction: Color = Color("#ffffe6")("businessInteraction"),
    businessInterface: Color = Color("#e6ffff")("businessInterface"),
    businessObject: Color = Color("#e6ffe6")("businessObject"),
    businessProcess: Color = Color("#ffffe6")("businessProcess"),
    businessRole: Color = Color("#e6ffff")("businessRole"),
    businessService: Color = Color("#ffffe6")("businessService"),
    contract: Color = Color("#e6ffe6")("contract"),
    dataObject: Color = Color("#afffaf")("dataObject"),
    device: Color = Color("#7dffff")("device"),
    technologyInteraction: Color = Color("#ffff82")("technologyInteraction"),
    technologyProcess: Color = Color("#ffff82")("technologyProcess"),
    technologyFunction: Color = Color("#ffff82")("technologyFunction"),
    technologyInterface: Color = Color("#7dffff")("technologyInterface"),
    technologyService: Color = Color("#ffff82")("technologyService"),
    technologyEvent: Color = Color("#ffff82")("technologyEvent"),
    location: Color = Color("#e6ffff")("location"),
    meaning: Color = Color("#e6ffe6")("meaning"),
    communicationNetwork: Color = Color("#7dffff")("communicationNetwork"),
    distributionNetwork: Color = Color("#7dffff")("distributionNetwork"),
    node: Color = Color("#7dffff")("node"),
    technologyCollaboration: Color =
      Color("#7dffff")("technologyCollaboration"),
    path: Color = Color("#7dffff")("path"),
    product: Color = Color("#e6ffe6")("product"),
    representation: Color = Color("#e6ffe6")("representation"),
    systemSoftware: Color = Color("#7dffff")("systemSoftware"),
    value: Color = Color("#e6ffe6")("value"),
    equipment: Color = Color("#7dffff")("equipment"),
    facility: Color = Color("#7dffff")("facility"),
    material: Color = Color("#91ff91")("material")
) {
  val toMap: Map[String, Color] = Map(
    applicationCollaboration.tuple,
    applicationComponent.tuple,
    externalApplicationComponent.tuple,
    applicationFunction.tuple,
    aplicationProcess.tuple,
    applicationEvent.tuple,
    applicationInteraction.tuple,
    applicationInterface.tuple,
    applicationService.tuple,
    artifact.tuple,
    businessActor.tuple,
    businessCollaboration.tuple,
    businessEvent.tuple,
    businessFunction.tuple,
    businessCapability.tuple,
    businessInteraction.tuple,
    businessInterface.tuple,
    businessObject.tuple,
    businessProcess.tuple,
    businessRole.tuple,
    businessService.tuple,
    contract.tuple,
    dataObject.tuple,
    device.tuple,
    technologyInteraction.tuple,
    technologyProcess.tuple,
    technologyFunction.tuple,
    technologyInterface.tuple,
    technologyService.tuple,
    technologyEvent.tuple,
    location.tuple,
    meaning.tuple,
    communicationNetwork.tuple,
    distributionNetwork.tuple,
    node.tuple,
    technologyCollaboration.tuple,
    path.tuple,
    product.tuple,
    representation.tuple,
    systemSoftware.tuple,
    value.tuple,
    equipment.tuple,
    facility.tuple,
    material.tuple
  )
}
