package model

import dev.wishingtree.branch.friday.{JsonCodec, JsonDecoder, JsonEncoder}
import dev.wishingtree.branch.friday.JsonCodec.given
import dev.wishingtree.branch.friday.JsonDecoder.given
import dev.wishingtree.branch.piggy.Sql

given JsonDecoder[Long] = summon[JsonDecoder[Int]].map(_.toLong)
given JsonEncoder[Long] = summon[JsonEncoder[Int]].contraMap(_.toInt)

case class Activity(id: Long, habit: Habit, description: String)derives JsonCodec


case class ActivityRecord(id: Long, habit: Habit, description: String)
