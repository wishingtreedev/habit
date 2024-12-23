package handlers

import dev.wishingtree.branch.friday.Json.JsonArray
import dev.wishingtree.branch.friday.JsonCodec.given
import dev.wishingtree.branch.friday.{Json, JsonCodec, JsonDecoder, JsonEncoder}
import dev.wishingtree.branch.macaroni.fs.PathOps.*
import dev.wishingtree.branch.macaroni.poolers.ResourcePool
import dev.wishingtree.branch.spider.server.RequestHandler.given
import dev.wishingtree.branch.spider.server.Response.html
import dev.wishingtree.branch.spider.server.{Request, RequestHandler, Response}
import model.Habit.{Diet, Exercise, Kindness, Organization, Sleep}
import model.{ActivitySqLiteRepository, Habit}

import java.sql.{Connection, DriverManager}
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

object HabitRequestHandler extends RequestHandler[Unit, String] {

  given connPool: ResourcePool[Connection] = new ResourcePool[Connection] {

    val dbPath = wd / "site" / "sql" / "habit.sqlite"

    override def acquire: Connection =
      DriverManager.getConnection(s"jdbc:sqlite:${dbPath.toString}")

    override def release(resource: Connection): Unit = resource.close()
  }

  override def handle(request: Request[Unit]): Response[String] = {

    val activities =
      for {
        sleep        <- ActivitySqLiteRepository.getNActivities(Sleep, 5)
        exercise     <- ActivitySqLiteRepository.getNActivities(Exercise, 5)
        diet         <- ActivitySqLiteRepository.getNActivities(Diet, 5)
        kindness     <- ActivitySqLiteRepository.getNActivities(Kindness, 5)
        organization <- ActivitySqLiteRepository.getNActivities(Organization, 5)
      } yield {
        for (i <- 0 to 4) yield {
          s"""
             |<tr>
             |<td><div>${sleep(i).description}</div></td>
             |<td><div>${exercise(i).description}</div></td>
             |<td><div>${diet(i).description}</div></td>
             |<td><div>${kindness(i).description}</div></td>
             |<td><div>${organization(i).description}</div></td>
             |</tr>
             |""".stripMargin
        }.mkString("")
      }

    val content = activities.executePool
      .map(stuff => stuff.mkString(""))
      .getOrElse("SQL broke :sad:")

    html"""
          <table>
                    <thead>
                    <tr>
                        <th>H</th>
                        <th>A</th>
                        <th>B</th>
                        <th>I</th>
                        <th>T</th>
                    </tr>
                    </thead>
                    <tbody>
                    $content
                    </tbody>
                </table>
        """

  }
}
