lazy val root = (project in file("."))
  .settings(
    name := """btlr""",
    version := "1.0",
    scalaVersion := "2.12.8",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Seq(
      guice,
      "com.google.firebase" % "firebase-admin" % "6.14.0",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
    ),
    excludeDependencies ++= Seq(
      "com.google.protobuf" % "protobuf-java",
      "commons-logging" % "commons-logging"
    ),
    assemblyJarName := "btlr.jar",
    assemblyMergeStrategy in assembly := {
      case "META-INF/io.netty.versions.properties" => MergeStrategy.first
      case "module-info.class"                     => MergeStrategy.first
      case "play/reference-overrides.conf"              => MergeStrategy.first
      case x                                       => (assemblyMergeStrategy in assembly).value(x)
    }
  )
  .enablePlugins(PlayScala)
