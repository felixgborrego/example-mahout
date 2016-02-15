import sparkec2.SparkTaskKeys._
import sparkec2.Ec2SparkPluginSettings._
import AssemblyKeys._
import sbtassembly.Plugin.MergeStrategy


name := "example-mahout"

assemblySettings

sparkSettings

version := "1.0"

scalaVersion := "2.10.4"

resolvers += Resolver.mavenLocal


scalacOptions += "-target:jvm-1.7"

offline := true



libraryDependencies ++= Seq(
  ("org.apache.spark" %% "spark-core" % "1.0.1" % "provided" )
)

libraryDependencies += "org.apache.mahout" % "mahout-mrlegacy" % "1.0-SNAPSHOT"

libraryDependencies += "org.apache.mahout" % "mahout-integration" % "1.0-SNAPSHOT"

libraryDependencies += "org.apache.mahout" %% "mahout-math-scala" % "1.0-SNAPSHOT"

libraryDependencies += "org.apache.mahout" %% "mahout-spark" % "1.0-SNAPSHOT"




mergeStrategy in assembly <<= (mergeStrategy in assembly) {
  (old) => {
    case PathList("com","hadoop", xs @ _*) => MergeStrategy.discard
    case PathList("org","hsqldb", xs @ _*) => MergeStrategy.discard
    case PathList("org","jboss", xs @ _*) => MergeStrategy.discard
    case PathList("org","mortbay", xs @ _*) => MergeStrategy.discard
    case PathList("org","objectweb", xs @ _*) => MergeStrategy.discard
    case PathList("org","objenesis", xs @ _*) => MergeStrategy.discard
    case PathList("org","slf4j", xs @ _*) => MergeStrategy.discard
    case PathList("org","znerd", xs @ _*) => MergeStrategy.discard
    case PathList("thrift", xs @ _*) => MergeStrategy.discard
    case PathList("junit", xs @ _*) => MergeStrategy.discard
    case PathList("org", "apache","jasper", xs @ _*) => MergeStrategy.first
    case PathList("org", "apache","commons", xs @ _*) => MergeStrategy.first
    case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.first
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.discard
    case "application.conf" => MergeStrategy.concat
    case "unwanted.txt"     => MergeStrategy.discard
    // case "project.clj" => MergeStrategy.discard // Leiningen build files
    case x if x.startsWith("META-INF") => MergeStrategy.discard // Bumf
    case x if x.endsWith(".html") => MergeStrategy.discard // More bumf
    case PathList("com", "esotericsoftware", xs@_*) => MergeStrategy.last // For Log$Logger.class
    case x => MergeStrategy.first
  }
}
