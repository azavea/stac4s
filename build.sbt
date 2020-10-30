import xerial.sbt.Sonatype._

lazy val commonSettings = Seq(
  // We are overriding the default behavior of sbt-git which, by default, only
  // appends the `-SNAPSHOT` suffix if there are uncommitted changes in the
  // workspace.
  version := {
    if (git.gitDescribedVersion.value.isEmpty)
      git.gitHeadCommit.value.get.substring(0, 7) + "-SNAPSHOT"
    else if (git.gitCurrentTags.value.isEmpty || git.gitUncommittedChanges.value)
      git.gitDescribedVersion.value.get + "-SNAPSHOT"
    else
      git.gitDescribedVersion.value.get
  },
  scalaVersion := "2.12.11",
  cancelable in Global := true,
  scalafmtOnCompile := true,
  scapegoatVersion in ThisBuild := Versions.ScapegoatVersion,
  scapegoatDisabledInspections := Seq("ObjectNames", "EmptyCaseClass"),
  unusedCompileDependenciesFilter -= moduleFilter("com.sksamuel.scapegoat", "scalac-scapegoat-plugin"),
  addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.11.0" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
  addCompilerPlugin(
    "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
  ),
  addCompilerPlugin(scalafixSemanticdb),
  autoCompilerPlugins := true,
  externalResolvers := Seq(
    DefaultMavenRepository,
    Resolver.sonatypeRepo("snapshots"),
    Resolver.typesafeIvyRepo("releases"),
    Resolver.bintrayRepo("azavea", "maven"),
    Resolver.bintrayRepo("azavea", "geotrellis"),
    "locationtech-releases" at "https://repo.locationtech.org/content/groups/releases",
    "locationtech-snapshots" at "https://repo.locationtech.org/content/groups/snapshots",
    Resolver.bintrayRepo("guizmaii", "maven"),
    Resolver.bintrayRepo("colisweb", "maven"),
    "jitpack".at("https://jitpack.io"),
    Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/local"))(
      Resolver.ivyStylePatterns
    )
  )
)

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

lazy val publishSettings = Seq(
  organization := "com.azavea.stac4s",
  organizationName := "Azavea",
  organizationHomepage := Some(new URL("https://azavea.com/")),
  description := "stac4s is a scala library with primitives to build applications using the SpatioTemporal Asset Catalogs specification",
  publishArtifact in Test := false
) ++ sonatypeSettings ++ credentialSettings

lazy val sonatypeSettings = Seq(
  publishMavenStyle := true,
  sonatypeProfileName := "com.azavea",
  sonatypeProjectHosting := Some(GitHubHosting(user = "azavea", repository = "stac4s", email = "systems@azavea.com")),
  developers := List(
    Developer(
      id = "cbrown",
      name = "Christopher Brown",
      email = "cbrown@azavea.com",
      url = url("https://github.com/notthatbreezy")
    ),
    Developer(
      id = "jsantucci",
      name = "James Santucci",
      email = "jsantucci@azavea.com",
      url = url("https://github.com/jisantuc")
    ),
    Developer(
      id = "aaronxsu",
      name = "Aaron Su",
      email = "asu@azavea.com",
      url = url("https://github.com/aaronxsu")
    ),
    Developer(
      id = "azavea",
      name = "Azavea Inc.",
      email = "systems@azavea.com",
      url = url("https://www.azavea.com")
    )
  ),
  licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  publishTo := sonatypePublishTo.value
)

lazy val credentialSettings = Seq(
  credentials ++= List(
    for {
      id <- sys.env.get("GPG_KEY_ID")
    } yield Credentials("GnuPG Key ID", "gpg", id, "ignored"),
    for {
      user <- sys.env.get("SONATYPE_USERNAME")
      pass <- sys.env.get("SONATYPE_PASSWORD")
    } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", user, pass)
  ).flatten
)

val coreDependencies = Seq(
  "com.beachape"                %% "enumeratum"        % Versions.EnumeratumVersion,
  "com.beachape"                %% "enumeratum-circe"  % Versions.EnumeratumVersion,
  "com.chuusai"                 %% "shapeless"         % Versions.ShapelessVersion,
  "eu.timepit"                  %% "refined"           % Versions.RefinedVersion,
  "io.circe"                    %% "circe-core"        % Versions.CirceVersion,
  "io.circe"                    %% "circe-generic"     % Versions.CirceVersion,
  "io.circe"                    %% "circe-parser"      % Versions.CirceVersion,
  "io.circe"                    %% "circe-refined"     % Versions.CirceVersion,
  "org.locationtech.geotrellis" %% "geotrellis-vector" % Versions.GeoTrellisVersion,
  "org.locationtech.jts"         % "jts-core"          % Versions.Jts,
  "org.typelevel"               %% "cats-core"         % Versions.CatsVersion,
  "org.typelevel"               %% "cats-kernel"       % Versions.CatsVersion
)

val testingDependencies = Seq(
  "com.beachape"                %% "enumeratum"            % Versions.EnumeratumVersion,
  "com.beachape"                %% "enumeratum-scalacheck" % Versions.EnumeratumVersion,
  "com.chuusai"                 %% "shapeless"             % Versions.ShapelessVersion,
  "eu.timepit"                  %% "refined-scalacheck"    % Versions.RefinedVersion,
  "eu.timepit"                  %% "refined"               % Versions.RefinedVersion,
  "io.chrisdavenport"           %% "cats-scalacheck"       % Versions.ScalacheckCatsVersion,
  "io.circe"                    %% "circe-core"            % Versions.CirceVersion,
  "org.locationtech.geotrellis" %% "geotrellis-vector"     % Versions.GeoTrellisVersion,
  "org.locationtech.jts"         % "jts-core"              % Versions.Jts,
  "org.scalacheck"              %% "scalacheck"            % Versions.ScalacheckVersion,
  "org.typelevel"               %% "cats-core"             % Versions.CatsVersion
)

val testRunnerDependenciesJVM = Seq(
  "io.circe"          %% "circe-testing"   % Versions.CirceVersion            % Test,
  "org.scalatest"     %% "scalatest"       % Versions.ScalatestVersion        % Test,
  "org.scalatestplus" %% "scalacheck-1-14" % Versions.ScalatestPlusScalacheck % Test
)

lazy val root = project
  .in(file("."))
  .settings(moduleName := "root")
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(noPublishSettings)
  .aggregate(coreJvm, testingJvm, coreTestJvm)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .in(file("modules/core"))
  .settings(commonSettings)
  .settings(publishSettings)
  .settings({
    libraryDependencies ++= coreDependencies
  })

lazy val coreJvm = core.jvm
lazy val coreJs  = core.js

lazy val testing = (crossProject(JSPlatform, JVMPlatform))
  .in(file("modules/testing"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(libraryDependencies ++= testingDependencies)

lazy val testingJvm = testing.jvm

lazy val coreTest = crossProject(JSPlatform, JVMPlatform)
  .in(file("modules/core-test"))
  .dependsOn(testing % Test)
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(libraryDependencies ++= testRunnerDependenciesJVM)
  .jvmSettings(
    libraryDependencies ++= testRunnerDependenciesJVM
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "io.circe"          %%% "circe-testing"   % Versions.CirceVersion            % Test,
      "org.scalatest"     %%% "scalatest"       % Versions.ScalatestVersion        % Test,
      "org.scalatestplus" %%% "scalacheck-1-14" % Versions.ScalatestPlusScalacheck % Test
    )
  )

lazy val coreTestJvm = coreTest.jvm
