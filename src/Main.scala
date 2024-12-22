import dev.wishingtree.branch.macaroni.fs.PathOps.*
import dev.wishingtree.branch.spider.HttpMethod
import dev.wishingtree.branch.spider.server.{
  ContextHandler,
  FileContextHandler,
  SpiderApp
}

object Main extends SpiderApp {

  val fileHandler =
    FileContextHandler(
      wd / "site",
      filters = ContextHandler.timingFilter :: Nil
    )

  ContextHandler.registerHandler(fileHandler)

}
