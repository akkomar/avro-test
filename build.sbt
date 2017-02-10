name := "avro-test"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.julianpeeters" %% "avrohugger-core" % "0.14.0"
)

//https://github.com/julianpeeters/sbt-avrohugger
sbtavrohugger.SbtAvrohugger.avroSettings
//sbtavrohugger.SbtAvrohugger.specificAvroSettings
//sbtavrohugger.SbtAvrohugger.scavroSettings
