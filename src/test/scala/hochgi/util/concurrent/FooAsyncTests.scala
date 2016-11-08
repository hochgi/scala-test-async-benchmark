package hochgi.util.concurrent

import hochgi.util.concurrent.SimpleScheduler.{schedule => delayedTask}
import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Proj: scalaTestAsyncBenchmark
  * User: gilad
  * Date: 11/8/16
  * Time: 7:13 AM
  */
class FooAsyncTests extends AsyncFunSpec with Matchers {
  describe("asyncTest #1") {
    val f = delayedTask(1.second)("foo")
    it("should create an async test that matches for pre-cooked future foo"){
      f.map(_ should be("foo"))
    }
    it("should create an async test that not matches 'foo' to 'bar'"){
      f.flatMap { foo =>
        delayedTask(1.second){
          foo should not equal "bar"
        }
      }
    }
    it("should create an async test that not matches 'foo' to 'baz'"){
      f.flatMap { foo =>
        delayedTask(1.second){
          foo should not equal "baz"
        }
      }
    }
  }
}
