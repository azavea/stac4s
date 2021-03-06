package com.azavea.stac4s.meta

import cats.Eq
import cats.syntax.either._
import io.circe._
import io.circe.parser.decode

import scala.util.Try

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}
import java.time.{Instant, OffsetDateTime}

trait ForeignImplicits {

  // circe codecs
  // A more flexible alternative to DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS[xxx][xx][X]")
  // https://tools.ietf.org/html/rfc3339
  // Warning: This formatter is good only for parsing
  val RFC3339formatter =
    new DateTimeFormatterBuilder()
      .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
      .optionalStart()
      .appendOffset("+HH:MM", "+00:00")
      .optionalEnd()
      .optionalStart()
      .appendOffset("+HHMM", "+0000")
      .optionalEnd()
      .optionalStart()
      .appendOffset("+HH", "Z")
      .optionalEnd()
      .toFormatter()

  implicit val eqInstant: Eq[Instant] = Eq.fromUniversalEquals

  implicit val encodeInstant: Encoder[Instant] = Encoder[String].contramap(_.toString)

  implicit val decodeInstant: Decoder[Instant] =
    Decoder[String].emap(s =>
      Either
        .fromTry(Try(OffsetDateTime.parse(s, RFC3339formatter).toInstant))
        .leftMap(_ => s"$s was not a valid string format")
    )

  implicit val decTimeRange: Decoder[(Option[Instant], Option[Instant])] = Decoder[String] map { str =>
    val components = str.replace("[", "").replace("]", "").split(",") map {
      _.trim
    }
    components match {
      case parts if parts.length == 2 =>
        val start = parts.head
        val end   = parts.drop(1).head
        (decode[Instant](start).toOption, decode[Instant](end).toOption)
      case parts if parts.length > 2 =>
        val message = s"Too many elements for temporal extent: $parts"
        throw new ParsingFailure(message, new Exception(message))
      case parts if parts.length < 2 =>
        val message = s"Too few elements for temporal extent: $parts"
        throw new ParsingFailure(message, new Exception(message))
      case x => throw new scala.MatchError(x)
    }
  }

}

object ForeignImplicits extends ForeignImplicits {}
