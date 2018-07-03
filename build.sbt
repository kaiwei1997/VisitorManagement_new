name := "VisitorManagement"

version := "1.0"

scalaVersion := "2.12.3"

offline := true

resolvers += "Local Maven Repository" at "file:///" + Path.userHome + "/.ivy2/cache"
unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

libraryDependencies ++= Seq(
  "org.scalafx" % "scalafxml-core-sfx8_2.12" % "0.4",
  "org.scalafx" % "scalafx_2.12" % "8.0.144-R12",
  "com.jfoenix" % "jfoenix" % "9.0.0",
  "com.sun.mail" % "javax.mail" % "1.6.1",
  "org.apache.commons" % "commons-email" % "1.5"
)


fork := true
        