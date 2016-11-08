# scalaTest Async Benchmark
This is an example of how to best utilize scalaTest async abilities.
There are 3 similar tests, all simple tests that are executed asynchronously on a scheduler.
### test dependent on each other in the following way:
```none
    test1
      |
     / \
test2   test3
```
all tests takes 1 seconds to complete.
## in SBT shell
### when logic is inside scalaTest's test function:
```
> test:testOnly hochgi.util.concurrent.FooAsyncTests
[info] FooAsyncTests:
[info] asyncTest #1
[info] - should create an async test that matches for pre-cooked future foo
[info] - should create an async test that not matches 'foo' to 'bar'
[info] - should create an async test that not matches 'foo' to 'baz'
[info] Run completed in 3 seconds, 147 milliseconds.
[info] Total number of tests run: 3
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 3, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 4 s, completed Nov 8, 2016 8:45:29 AM
```
i.e: a test that could potentially execute in 2 seconds (parallelizing test2 & test3), takes 3 seconds to complete.
this can be improved if we execute the test functions outside the `ItWord`'s apply:
### when logic is executed outside scalaTest's test function:
```
> test:testOnly hochgi.util.concurrent.BarAsyncTests
[info] BarAsyncTests:
[info] asyncTest #2
[info] - should create an async test that matches for pre-cooked future bar
[info] - should create an async test that not matches 'bar' to 'foo'
[info] - should create an async test that not matches 'bar' to 'baz'
[info] Run completed in 2 seconds, 140 milliseconds.
[info] Total number of tests run: 3
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 3, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 8 s, completed Nov 8, 2016 9:00:47 AM
```
So this is exactly what we wanted. tests are being run concurrently.
### adding a third test that operates same as first test and thus should take 3 seconds, and running it all together:
```
> test
[info] BarAsyncTests:
[info] asyncTest #2
[info] - should create an async test that matches for pre-cooked future bar
[info] - should create an async test that not matches 'bar' to 'foo'
[info] - should create an async test that not matches 'bar' to 'baz'
[info] FooAsyncTests:
[info] asyncTest #1
[info] - should create an async test that matches for pre-cooked future foo
[info] - should create an async test that not matches 'foo' to 'bar'
[info] - should create an async test that not matches 'foo' to 'baz'
[info] BazAsyncTests:
[info] asyncTest #3
[info] - should create an async test that matches for pre-cooked future baz
[info] - should create an async test that not matches 'baz' to 'bar'
[info] - should create an async test that not matches 'baz' to 'baz'
[info] Run completed in 3 seconds, 131 milliseconds.
[info] Total number of tests run: 9
[info] Suites: completed 3, aborted 0
[info] Tests: succeeded 9, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 6 s, completed Nov 8, 2016 9:03:49 AM
```
turns out all suites run concurrently, and overall execution time is 3 seconds (with the addition of the actual run time).

## conclusion
- Break down big test suites to smaller chunks that can run concurrently.
- Inside suites that have complicated dependencies between tests, execute it outside of scalatest's expressions.
