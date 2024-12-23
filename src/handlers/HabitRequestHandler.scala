package handlers

import dev.wishingtree.branch.spider.server.RequestHandler.given
import dev.wishingtree.branch.spider.server.Response.html
import dev.wishingtree.branch.spider.server.{Request, RequestHandler, Response}
import model.Habit.{Diet, Exercise, Kindness, Organization, Sleep}
import model.SQLite.given
import model.{ActivitySqLiteRepository, Habit}

object HabitRequestHandler extends RequestHandler[Unit, String] {

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
      .map(rows => rows.mkString(""))

    content
      .map { tableData =>
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
                            $tableData
                          </tbody>
                      </table>
              """
      }
      .getOrElse(html"<h1>Failed to retrieve activities 😭</h1>")

  }
}
