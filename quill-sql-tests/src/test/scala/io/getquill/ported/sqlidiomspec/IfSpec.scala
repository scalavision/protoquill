package io.getquill.ported.sqlidiomspec

import scala.language.implicitConversions
import io.getquill.ReturnAction.ReturnColumns
//import io.getquill.MirrorSqlDialectWithReturnMulti
import io.getquill.context.mirror.Row
import io.getquill.context.sql.testContext
import io.getquill.context.sql.testContext._
import io.getquill._

class ActionSpec extends Spec {
  "if" - {
    "simple" in {
      inline def q = quote {
        qr1.map(t => if (t.i > 0) "a" else "b")
      }
      testContext.run(q).string mustEqual
        "SELECT CASE WHEN t.i > 0 THEN 'a' ELSE 'b' END FROM TestEntity t"
    }
    "simple booleans" in {
      inline def q = quote {
        qr1.map(t => if (true) true else false)
      }
      testContext.run(q).string mustEqual
        "SELECT CASE WHEN true THEN true ELSE false END FROM TestEntity t"
    }
    "nested" in {
      inline def q = quote {
        qr1.map(t => if (t.i > 0) "a" else if (t.i > 10) "b" else "c")
      }
      testContext.run(q).string mustEqual
        "SELECT CASE WHEN t.i > 0 THEN 'a' WHEN t.i > 10 THEN 'b' ELSE 'c' END FROM TestEntity t"
    }
    "nested booleans" in {
      inline def q = quote {
        qr1.map(t => if (true) true else if (true) true else false)
      }
      testContext.run(q).string mustEqual
        "SELECT CASE WHEN true THEN true WHEN true THEN true ELSE false END FROM TestEntity t"
    }
  }
}