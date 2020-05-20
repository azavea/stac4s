package com.azavea.stac4s.extensions.eo

import com.azavea.stac4s.extensions.ItemAssetExtension

import cats.Eq
import cats.data.NonEmptyList
import io.circe._
import io.circe.refined._
import io.circe.syntax._

case class EOAssetExtension(
    bands: NonEmptyList[BandRange]
)

object EOAssetExtension {

  implicit val eqEOAssetExtension: Eq[EOAssetExtension] = Eq.fromUniversalEquals

  implicit val encEOAssetExtension: Encoder.AsObject[EOAssetExtension] =
    Encoder.AsObject[Map[String, Json]].contramapObject(assetExt => Map("eo:bands" -> assetExt.bands.asJson))

  implicit val decEOAssetExtension: Decoder[EOAssetExtension] = Decoder.forProduct1("eo:bands")(EOAssetExtension.apply)

  implicit val assetExtension: ItemAssetExtension[EOAssetExtension] = ItemAssetExtension.instance
}
