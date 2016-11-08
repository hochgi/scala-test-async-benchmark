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
class BazAsyncTests extends AsyncFunSpec with Matchers {
  describe("asyncTest #3") {
    val f = delayedTask(1.second)("baz")
    it("should create an async test that matches for pre-cooked future baz"){
      f.map(_ should not equal "foo")
    }
    it("should create an async test that not matches 'baz' to 'bar'"){
      f.flatMap { baz =>
        delayedTask(1.second){
          baz should not equal "bar"
        }
      }
    }
    it("should create an async test that not matches 'baz' to 'baz'"){
      f.flatMap { baz =>
        delayedTask(1.second){
          baz should be("baz")
        }
      }
    }
  }
}
