package com.azavea.stac4s.extensions.layer

import com.azavea.stac4s.geometry.Geometry
import com.azavea.stac4s.StacLink

import cats.kernel.Eq
import eu.timepit.refined.types.string
import io.circe.refined._
import io.circe.{Decoder, Encoder}

final case class StacLayer(
    id: string.NonEmptyString,
    geometry: Geometry,
    properties: StacLayerProperties,
    links: List[StacLink],
    _type: String = "Feature"
)

object StacLayer {
  implicit val eqStacLayer: Eq[StacLayer] = Eq.fromUniversalEquals

  implicit val encStacLayer: Encoder[StacLayer] = Encoder.forProduct5(
    "id",
    "geometry",
    "properties",
    "links",
    "type"
  )(layer => (layer.id, layer.geometry, layer.properties, layer.links, layer._type))

  implicit val decStacLayer: Decoder[StacLayer] = Decoder.forProduct5(
    "id",
    "geometry",
    "properties",
    "links",
    "type"
  )(StacLayer.apply)
}
