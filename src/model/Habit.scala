package model

import dev.wishingtree.branch.friday.{Json, JsonCodec, JsonDecoder, JsonEncoder}

import scala.util.Try

enum Habit {
  case Sleep, Exercise, Diet, Kindness, Organization, Random
}

object Habit {
  given Conversion[Array[Byte], Habit] with {
    def apply(bytes: Array[Byte]): Habit = {
      val str = String(bytes)
      str match {
        case "Sleep" => Sleep
        case "Exercise" => Exercise
        case "Diet" => Diet
        case "Kindness" => Kindness
        case "Organization" => Organization
        case "Random" => Random
        case _ => Random
      }
    }
  }

  given JsonCodec[Habit] = JsonCodec(using
    summon[JsonEncoder[String]].contraMap(_.toString),
    summon[JsonDecoder[String]].map(Habit.valueOf)
  )

}