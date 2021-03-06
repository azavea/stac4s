package com.azavea.stac4s.extensions.layer

import com.azavea.stac4s.extensions.ItemExtension

import cats.Eq
import cats.data.NonEmptyList
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.refined._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

case class LayerItemExtension(ids: NonEmptyList[NonEmptyString])

object LayerItemExtension {
  implicit val eqLayerProperties: Eq[LayerItemExtension] = Eq.fromUniversalEquals

  implicit val encLayerProperties: Encoder.AsObject[LayerItemExtension] =
    Encoder.AsObject.instance[LayerItemExtension] { o => Map("layer:ids" -> o.ids.asJson).asJsonObject }

  implicit val decLayerProperties: Decoder[LayerItemExtension] =
    Decoder.forProduct1("layer:ids")(LayerItemExtension.apply)

  implicit val itemExtension: ItemExtension[LayerItemExtension] = ItemExtension.instance
}
