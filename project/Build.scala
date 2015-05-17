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
      libraryDependencies   += "org.jsoup"               % "jsoup"                   % "1.7.2",
      libraryDependencies   += "org.fusesource.jansi"    % "jansi"                   % "1.11",
      libraryDependencies   += "org.scala-lang.modules" %% "scala-xml"               % "1.0.3",
      libraryDependencies   += "mysql"                   %  "mysql-connector-java"   % "5.1.33",
      libraryDependencies   += "com.typesafe.slick"     %%  "slick"                  % "2.1.0",
      libraryDependencies   += "org.slf4j"               % "slf4j-nop"               % "1.6.4",
      libraryDependencies   += "org.flywaydb"            % "flyway-core"             % "3.2.1",
      libraryDependencies   += "com.plasmaconduit"      %% "json-config"             % "0.1.0",
      libraryDependencies   += "com.plasmaconduit"      %% "url"                     % "0.15.0"
    )

}
