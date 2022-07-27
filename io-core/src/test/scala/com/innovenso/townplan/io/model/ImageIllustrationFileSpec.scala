package com.innovenso.townplan.io.model

import com.innovenso.townplan.io.illustration.ImageIllustrationFile
import com.innovenso.townplanner.model.concepts.properties.ImageIllustration
import org.scalatest.flatspec.AnyFlatSpec

class ImageIllustrationFileSpec extends AnyFlatSpec {
  "image file" should "be readable for illustration" in {
    val illustration: ImageIllustration =
      ImageIllustration(imagePath = "image/test/townplan.png")
    assert(ImageIllustrationFile(illustration).load.exists(file => {
      println(file.getAbsolutePath)
      file.canRead
    }))
  }
}
