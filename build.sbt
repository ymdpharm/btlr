lazy val root = (project in file("."))
  .settings(
    name := """btlr""",
    version := "1.0",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      guice,
      "com.google.firebase" % "firebase-admin" % "6.14.0",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
    )
  )
  .enablePlugins(PlayScala)
