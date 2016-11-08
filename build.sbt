// give the user a nice default project!
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.hochgi",
      scalaVersion := "2.11.8"
    )),
    name := "scalaTestAsyncBenchmark",
    libraryDependencies ++= Seq(
      "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test",
      "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.5.0")
  )
