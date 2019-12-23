import Versions._
import Deps._
lazy val root = (project in file(".")).settings(
  name:="eric_project",
  version:="1.0",
  scalaVersion :=scalaV,
  libraryDependencies ++= allDeps
)