import dev.wishingtree.branch.macaroni.fs.PathOps.*
import dev.wishingtree.branch.spider.HttpMethod
import dev.wishingtree.branch.spider.server.{
  ContextHandler,
  FileContextHandler,
  SpiderApp
}
import handlers.HabitContextHandler

object Main extends SpiderApp {

  val combined: ContextHandler =
    FileContextHandler(
      wd / "site",
      filters = ContextHandler.timingFilter :: Nil
    ) |+| HabitContextHandler

  ContextHandler.registerHandler(combined)

}
