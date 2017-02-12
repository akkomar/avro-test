name := "avro-test"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.julianpeeters" %% "avrohugger-core" % "0.14.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

//https://github.com/julianpeeters/sbt-avrohugger
//sbtavrohugger.SbtAvrohugger.avroSettings
sbtavrohugger.SbtAvrohugger.specificAvroSettings
//sbtavrohugger.SbtAvrohugger.scavroSettings


PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)