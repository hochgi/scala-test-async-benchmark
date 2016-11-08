package hochgi.util.concurrent

import hochgi.util.concurrent.SimpleScheduler.{schedule => delayedTask}
import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Proj: scalaTestAsyncBenchmark
  * User: gilad
  * Date: 11/8/16
  * Time: 7:30 AM
  */
class BarAsyncTests extends AsyncFunSpec with Matchers {
  describe("asyncTest #2") {
    val f0 = delayedTask(1.second)("bar")
    val f1 = f0.flatMap { bar =>
      delayedTask(1.second){
        bar should not equal "foo"
      }
    }
    val f2 = f0.flatMap { bar =>
      delayedTask(1.second){
        bar should not equal "baz"
      }
    }

    it("should create an async test that matches for pre-cooked future bar"){
      f0.map(_ should be("bar"))
    }
    it("should create an async test that not matches 'bar' to 'foo'")(f1)
    it("should create an async test that not matches 'bar' to 'baz'")(f2)
  }
}
