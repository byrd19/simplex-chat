import org.jetbrains.compose.compose

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
  id("com.android.library")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("dev.icerock.mobile.multiplatform-resources")
  id("io.github.tomtzook.gradle-cmake") version "1.2.2"
}

group = "chat.simplex"
version = extra["app.version_name"] as String

kotlin {
  android()
  jvm("desktop") {
    jvmToolchain(11)
  }
  sourceSets {
    all {
      languageSettings {
        optIn("kotlinx.coroutines.DelicateCoroutinesApi")
        optIn("androidx.compose.foundation.ExperimentalFoundationApi")
        optIn("androidx.compose.ui.text.ExperimentalTextApi")
        optIn("androidx.compose.material.ExperimentalMaterialApi")
        optIn("com.arkivanov.decompose.ExperimentalDecomposeApi")
        optIn("kotlinx.serialization.InternalSerializationApi")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
        optIn("androidx.compose.ui.ExperimentalComposeUiApi")
        optIn("com.google.accompanist.permissions.ExperimentalPermissionsApi")
      }
    }

    val commonMain by getting {
      kotlin.srcDir("./build/generated/moko/commonMain/src/")
      dependencies {
        api(compose.runtime)
        api(compose.foundation)
        api(compose.material)
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
        api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
        api("com.russhwolf:multiplatform-settings:1.0.0")
        api("com.charleskorn.kaml:kaml:0.43.0")
        api("dev.icerock.moko:resources-compose:0.22.3")
        api("org.jetbrains.compose.ui:ui-text:${rootProject.extra["compose_version"] as String}")
        implementation("org.jetbrains.compose.components:components-animatedimage:${rootProject.extra["compose_version"] as String}")
        //Barcode
        api("org.boofcv:boofcv-core:0.40.1")
        implementation("com.godaddy.android.colorpicker:compose-color-picker-jvm:0.7.0")
        // Link Previews
        implementation("org.jsoup:jsoup:1.13.1")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val androidMain by getting {
      dependencies {
        api("androidx.appcompat:appcompat:1.5.1")
        api("androidx.core:core-ktx:1.9.0")
        api("androidx.activity:activity-compose:1.5.0")
        val work_version = "2.7.1"
        api("androidx.work:work-runtime-ktx:$work_version")
        api("androidx.work:work-multiprocess:$work_version")
        implementation("com.google.accompanist:accompanist-insets:0.23.0")
        implementation("dev.icerock.moko:resources:0.22.3")

        // Video support
        implementation("com.google.android.exoplayer:exoplayer:2.17.1")

        // Biometric authentication
        implementation("androidx.biometric:biometric:1.2.0-alpha04")

        //Barcode
        implementation("org.boofcv:boofcv-android:0.40.1")

        //Camera Permission
        api("com.google.accompanist:accompanist-permissions:0.23.0")

        implementation("androidx.webkit:webkit:1.4.0")

        // GIFs support
        implementation("io.coil-kt:coil-compose:2.1.0")
        implementation("io.coil-kt:coil-gif:2.1.0")

        val camerax_version = "1.1.0-beta01"
        implementation("androidx.camera:camera-core:${camerax_version}")
        implementation("androidx.camera:camera-camera2:${camerax_version}")
        implementation("androidx.camera:camera-lifecycle:${camerax_version}")
        implementation("androidx.camera:camera-view:${camerax_version}")
      }
    }
    val desktopMain by getting {
      dependencies {
        implementation("dev.icerock.moko:resources:0.22.3")
      }
    }
    val desktopTest by getting
  }
}

android {
  compileSdkVersion(33)
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdkVersion(26)
    targetSdkVersion(33)
    buildConfigField("String", "VERSION_NAME", "\"${extra["app.version_name"]}\"")
    buildConfigField("String", "VERSION_CODE", "\"${extra["app.version_code"]}\"")
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

// LALAL
/*
* compose {
    desktop {
        application {
            mainClass = "chat.simplex.common.MainKt"
            nativeDistributions {
                targetFormats(
                    org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                    org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                    org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
                )
                packageName = "simplex"
                version = "1.0.0"
            }
        }
    }
}
* */

cmake {
  // Run this command to make build for all targets:
  // ./gradlew common:cmakeBuild -PcrossCompile
  if (project.hasProperty("crossCompile")) {
    machines.customMachines.register("linux-amd64") {
      toolchainFile.set(project.file("../android/src/main/cpp/toolchains/x86_64-linux-gnu-gcc.cmake"))
    }
    /*machines.customMachines.register("linux-aarch64") {
      toolchainFile.set(project.file("../android/src/main/cpp/toolchains/aarch64-linux-gnu-gcc.cmake"))
    }*/
    machines.customMachines.register("win-amd64") {
      toolchainFile.set(project.file("../android/src/main/cpp/toolchains/x86_64-windows-mingw32-gcc.cmake"))
    }
    if (machines.host.name == "mac-amd64") {
      machines.customMachines.register("mac-amd64") {
        toolchainFile.set(project.file("../android/src/main/cpp/toolchains/x86_64-mac-apple-darwin-gcc.cmake"))
      }
    }
  }
  val compileMachineTargets = arrayListOf<com.github.tomtzook.gcmake.targets.TargetMachine>(machines.host)
  compileMachineTargets.addAll(machines.customMachines)
  targets {
    val main by creating {
      cmakeLists.set(file("../android/src/main/cpp/desktop/CMakeLists.txt"))
      targetMachines.addAll(compileMachineTargets.toSet())
    }
  }
}

tasks.named("clean") {
  dependsOn(":cmakeClean")
}

tasks.named("build") {
  dependsOn(":cmakeBuild")
}

multiplatformResources {
  multiplatformResourcesPackage = "com.icerockdev.library"
//  multiplatformResourcesClassName = "MR"
}