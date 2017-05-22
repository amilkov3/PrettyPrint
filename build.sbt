import Dependencies._

name := "prettyprint"

lazy val scalaV = "2.12.2"
lazy val scalaCheckVersion = "3.0.1"

scalaVersion in Global := scalaV

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.voklim",
      scalaVersion := scalaV,
      version      := "0.1.0"
    )),
    libraryDependencies ++= Seq(
      "com.chuusai" %% "shapeless" % "2.3.2",

      "org.scalatest" %% "scalatest" % scalaCheckVersion % "test",
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
    )
  )
