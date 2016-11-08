package hochgi.util.concurrent

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * Created by gilad on 12/3/15.
 */
object SimpleScheduler {
  private[this] lazy val timer = java.util.concurrent.Executors.newScheduledThreadPool(1)

  def schedule[T] (duration: FiniteDuration)(body: => T)(implicit executionContext: ExecutionContext): Future[T] = {
    val p = Promise[T]()
    timer.schedule(new Runnable {
      override def run(): Unit = {
        // body may be expensive to compute, and must not be run in our only timer thread expense,
        // so we compute the task inside a `Future` and make it run on the expense of the given executionContext.
        p.completeWith(Future(body)(executionContext))
      }
    },duration.toMillis,java.util.concurrent.TimeUnit.MILLISECONDS)
    p.future
  }

  def scheduleFuture[T](duration: FiniteDuration)(body: => Future[T]): Future[T] = {
    val p = Promise[T]()
    timer.schedule(new Runnable {
      override def run(): Unit = p.completeWith(body)
    },duration.toMillis,java.util.concurrent.TimeUnit.MILLISECONDS)
    p.future
  }
}
