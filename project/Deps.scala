import sbt._
import sbt.Keys._
object Versions{
    val scalaV = "2.13.1"
    val akkaV = "2.6.1"

}
object Deps {
    val typedActor =  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.1"
    //val slf4j="org.slf4j" % "slf4j-api" % "1.7.29"
    val logback="ch.qos.logback" % "logback-classic" % "1.2.3"
    val allDeps = Seq(typedActor,/*slf4j,*/logback)
}
