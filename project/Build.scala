import sbt._
import sbt.Keys._

import spray.revolver.RevolverPlugin._

object Build extends Build {

  lazy val root = Project("root", file("."))
    .settings(Revolver.settings: _*)
    .settings(
      name                  := "crawlman",
      organization          := "com.appdation",
      version               := "0.1.0",
      scalaVersion          := "2.11.4",
      licenses              += ("MIT", url("http://opensource.org/licenses/MIT")),
      resolvers             ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo),
      resolvers             += "Plasma Conduit Repository" at "http://dl.bintray.com/plasmaconduit/releases",
      resolvers             += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
      libraryDependencies   += "org.jsoup" % "jsoup" % "1.7.2",
      libraryDependencies   += "org.fusesource.jansi" % "jansi" % "1.11",
      libraryDependencies   += "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
      updateOptions         := updateOptions.value.withCachedResolution(true)
    )

}
