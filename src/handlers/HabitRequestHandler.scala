package handlers

import dev.wishingtree.branch.friday.Json.JsonArray
import dev.wishingtree.branch.friday.{Json, JsonCodec, JsonDecoder, JsonEncoder}
import dev.wishingtree.branch.friday.JsonCodec.*
import dev.wishingtree.branch.friday.JsonCodec.given
import dev.wishingtree.branch.friday.JsonEncoder.given
import dev.wishingtree.branch.friday.JsonDecoder.given
import dev.wishingtree.branch.spider.server.{Request, RequestHandler, Response}
import model.Habit

import scala.util.Try


case class Habits(activities: Seq[String]) derives JsonCodec

object Habits {
  
  given JsonCodec[Seq[String]] = new JsonCodec[Seq[String]] {

    override def decode(json: Json): Try[Seq[String]] =
      JsonDecoder.decode[Seq[String]](json)

    override def encode(a: Seq[String]): Json =
      JsonArray(a.toIndexedSeq.map(Json.JsonString(_)))
  }
  
  given Conversion[Habits, Array[Byte]] with {
    def apply(habits: Habits): Array[Byte] = 
      summon[JsonEncoder[Habits]].encode(habits).toJsonString.getBytes
  }
}

object HabitRequestHandler extends RequestHandler[Habit, Habits] {

  override def handle(request: Request[Habit]): Response[Habits] = ???
}