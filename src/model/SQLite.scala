package model

import dev.wishingtree.branch.macaroni.fs.PathOps.*
import dev.wishingtree.branch.macaroni.poolers.ResourcePool

import java.sql.{Connection, DriverManager}

object SQLite {
  given connPool: ResourcePool[Connection] = new ResourcePool[Connection] {

    val dbPath = wd / "site" / "sql" / "habit.sqlite"

    override def acquire: Connection =
      DriverManager.getConnection(s"jdbc:sqlite:${dbPath.toString}")

    override def release(resource: Connection): Unit = resource.close()
  }
}
