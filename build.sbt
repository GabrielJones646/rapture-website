lazy val root = (project in file(".")).
  settings(
    name := "rapturesite",
    version := "1.0-M1",
    scalaVersion := "2.11.7"
  )

unmanagedResourceDirectories in Compile += { baseDirectory.value / "ext" }

libraryDependencies += "com.propensive" %% "rapture" % "2.0.0-M2"
