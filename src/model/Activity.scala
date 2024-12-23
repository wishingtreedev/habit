package model

import dev.wishingtree.branch.friday.{JsonCodec, JsonDecoder, JsonEncoder}
import dev.wishingtree.branch.friday.JsonCodec.given
import dev.wishingtree.branch.friday.JsonDecoder.given
import dev.wishingtree.branch.piggy.Sql
import dev.wishingtree.branch.piggy.Sql.*

given JsonDecoder[Long] = summon[JsonDecoder[Int]].map(_.toLong)
given JsonEncoder[Long] = summon[JsonEncoder[Int]].contraMap(_.toInt)

case class Activity(id: Long, habit: Habit, description: String)
    derives JsonCodec

case class ActivityRecord(id: Long, habit: Habit, description: String)

object ActivityRecord {
  given Conversion[ActivityRecord, Activity] = ar =>
    Activity(ar.id, ar.habit, ar.description)
}

trait ActivityRepository {
  def getNActivities(habit: Habit, n: Int): Sql[Seq[ActivityRecord]]
}

object ActivitySqLiteRepository extends ActivityRepository {

  override def getNActivities(
      habit: Habit,
      n: Int
  ): Sql[Seq[ActivityRecord]] = {
    habit match
      case Habit.Random =>
        Sql
          .prepareQuery[Tuple1[Int], (Long, String, String)](
            a =>
              ps"SELECT id, habit, description FROM activities ORDER BY RANDOM() LIMIT $a",
            n.tuple1
          )
      case h            =>
        Sql
          .prepareQuery[(String, Int), (Long, String, String)](
            (h, a) =>
              ps"SELECT id, habit, description FROM activities WHERE habit = $h ORDER BY RANDOM() LIMIT $a",
            (habit.toString, n)
          )
  }.map(
    _.map { case (id, habit, description) =>
      ActivityRecord(id, Habit.valueOf(habit), description)
    }
  )
  
}
