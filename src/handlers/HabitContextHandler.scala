package handlers

import dev.wishingtree.branch.spider.HttpMethod
import dev.wishingtree.branch.spider.server.{ContextHandler, RequestHandler}

import java.nio.file.Path

object HabitContextHandler extends ContextHandler("/") {

  override val contextRouter: PartialFunction[(HttpMethod, Path), RequestHandler[_, _]] = ???
}