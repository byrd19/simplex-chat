import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
  id("io.github.tomtzook.gradle-cmake") version "1.2.2"
}

group = "chat.simplex"
version = extra["app.version_name"] as String


kotlin {
  jvm {
    jvmToolchain(11)
    withJava()
  }
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(project(":common"))
        implementation(compose.desktop.currentOs)
      }
    }
    val jvmTest by getting
  }
}

// https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Native_distributions_and_local_execution
compose {
  desktop {
    application {
      mainClass = "MainKt"
      nativeDistributions {
        outputBaseDir.set(project.file("../release"))
        targetFormats(
          TargetFormat.Deb, TargetFormat.AppImage, TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe
        )
        linux {
          iconFile.set(project.file("../common/src/commonMain/resources/distribute/simplex.png"))
          appCategory = "Messenger"
        }
        windows {
          // LALAL
          iconFile.set(project.file("../common/src/commonMain/resources/distribute/simplex.ico"))
          console = true
          perUserInstall = true
          dirChooser = true
        }
        macOS {
          // LALAL
          iconFile.set(project.file("../common/src/commonMain/resources/distribute/simplex.icns"))
          appCategory = "public.app-category.social-networking"
          bundleID = "chat.simplex.app"
        }
        packageName = "simplex"
        // Packaging requires to have version like MAJOR.MINOR.PATCH
        var adjustedVersion = rootProject.extra["app.version_name"] as String
        if (adjustedVersion.split(".").size != 3) {
          adjustedVersion += ".0"
        }
        version = adjustedVersion
      }
    }
  }
}

val cppPath = "../common/src/commonMain/cpp"
cmake {
  // Run this command to make build for all targets:
  // ./gradlew common:cmakeBuild -PcrossCompile
  if (project.hasProperty("crossCompile")) {
    machines.customMachines.register("linux-amd64") {
      toolchainFile.set(project.file("$cppPath/toolchains/x86_64-linux-gnu-gcc.cmake"))
    }
    /*machines.customMachines.register("linux-aarch64") {
      toolchainFile.set(project.file("$cppPath/toolchains/aarch64-linux-gnu-gcc.cmake"))
    }*/
    machines.customMachines.register("win-amd64") {
      toolchainFile.set(project.file("$cppPath/toolchains/x86_64-windows-mingw32-gcc.cmake"))
    }
    if (machines.host.name == "mac-amd64") {
      machines.customMachines.register("mac-amd64") {
        toolchainFile.set(project.file("$cppPath/toolchains/x86_64-mac-apple-darwin-gcc.cmake"))
      }
    }
  }
  val compileMachineTargets = arrayListOf<com.github.tomtzook.gcmake.targets.TargetMachine>(machines.host)
  compileMachineTargets.addAll(machines.customMachines)
  targets {
    val main by creating {
      cmakeLists.set(file("$cppPath/desktop/CMakeLists.txt"))
      targetMachines.addAll(compileMachineTargets.toSet())
    }
  }
}

tasks.named("clean") {
  dependsOn("cmakeClean")
}
tasks.named("compileJava") {
  dependsOn("cmakeBuildAndCopy")
}
afterEvaluate {
  tasks.create("cmakeBuildAndCopy") {
    dependsOn("cmakeBuild")
    doLast {
      copy {
        from("${project(":desktop").buildDir}/cmake/main/linux-amd64", "$cppPath/desktop/libs/linux-x86_64")
        into("../common/src/commonMain/resources/libs/linux-x86_64")
        include("*.so")
        eachFile {
          path = name
        }
        includeEmptyDirs = false
      }
      copy {
        from("${project(":desktop").buildDir}/cmake/main/linux-aarch64", "$cppPath/desktop/libs/linux-aarch64")
        into("../common/src/commonMain/resources/libs/linux-aarch64")
        include("*.so")
        eachFile {
          path = name
        }
        includeEmptyDirs = false
      }
      copy {
        from("${project(":desktop").buildDir}/cmake/main/win-amd64", "$cppPath/desktop/libs/windows-x86_64")
        into("../common/src/commonMain/resources/libs/windows-x86_64")
        include("*.dll")
        eachFile {
          path = name
        }
        includeEmptyDirs = false
      }
      copy {
        from("${project(":desktop").buildDir}/cmake/main/mac-amd64", "$cppPath/desktop/libs/mac-x86_64")
        into("../common/src/commonMain/resources/libs/mac-x86_64")
        include("*.dylib")
        eachFile {
          path = name
        }
        includeEmptyDirs = false
      }
    }
  }
}