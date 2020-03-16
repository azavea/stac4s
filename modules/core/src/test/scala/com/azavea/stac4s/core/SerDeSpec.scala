package com.azavea.stac4s

import com.azavea.stac4s.meta._
import com.azavea.stac4s.Generators._

import geotrellis.vector.Geometry
import io.circe.testing.{ArbitraryInstances, CodecTests}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.typelevel.discipline.scalatest.FunSuiteDiscipline

import java.time.Instant

class SerDeSpec extends AnyFunSuite with FunSuiteDiscipline with Checkers with ArbitraryInstances {

  checkAll("Codec.StacMediaType", CodecTests[StacMediaType].unserializableCodec)
  checkAll("Codec.StacAssetRole", CodecTests[StacAssetRole].unserializableCodec)
  checkAll("Codec.StacLinkType", CodecTests[StacLinkType].unserializableCodec)
  checkAll("Codec.StacProviderRole", CodecTests[StacProviderRole].unserializableCodec)

  checkAll("Codec.Instant", CodecTests[Instant].unserializableCodec)
  checkAll("Codec.Geometry", CodecTests[Geometry].unserializableCodec)

  checkAll("Codec.StacItemAsset", CodecTests[StacItemAsset].unserializableCodec)
  checkAll("Codec.StacCollectionAsset", CodecTests[StacCollectionAsset].unserializableCodec)

  checkAll("Codec.SPDX", CodecTests[SPDX].unserializableCodec)

  checkAll("Codec.StacItem", CodecTests[StacItem].unserializableCodec)

  checkAll("Codec.ItemCollection", CodecTests[ItemCollection].unserializableCodec)

  checkAll("Codec.StacCatalog", CodecTests[StacCatalog].unserializableCodec)

  checkAll("Codec.TwoDimBbox", CodecTests[TwoDimBbox].unserializableCodec)

  checkAll("Codec.ThreeDimBbox", CodecTests[ThreeDimBbox].unserializableCodec)

  checkAll("Codec.TemporalExtent", CodecTests[TemporalExtent].unserializableCodec)
  checkAll("Codec.Bbox", CodecTests[Bbox].unserializableCodec)
  checkAll("Codec.StacExtent", CodecTests[StacExtent].unserializableCodec)

  checkAll("Codec.StacCollection", CodecTests[StacCollection].unserializableCodec)

}
