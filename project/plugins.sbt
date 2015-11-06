addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "0.8.2")

resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")
