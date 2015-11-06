organization := "com.lunatech"

name := "play2-activiti"

version := "0.2.2-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Alfresco Maven Repository" at "https://maven.alfresco.com/nexus/content/groups/public/")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.4.3",
  "com.typesafe.play" %% "play-jdbc" % "2.4.3",
  "org.activiti" % "activiti-engine" % "5.18.0",
  // For the SquerylJoinedTransactionFactory
  "org.squeryl" %% "squeryl" % "0.9.5-7")

publishTo <<= version { (v: String) =>
  val path = if(v.trim.endsWith("SNAPSHOT")) "snapshots-public" else "releases-public"
  Some(Resolver.url("Lunatech Artifactory", new URL("http://artifactory.lunatech.com/artifactory/%s/" format path)))
}

site.settings

site.includeScaladoc()

ghpages.settings

git.remoteRepo := "git@github.com:lunatech-labs/play2-activiti.git"
