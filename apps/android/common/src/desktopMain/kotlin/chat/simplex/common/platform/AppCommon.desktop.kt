package chat.simplex.common.platform

import chat.simplex.common.DesktopApp
import chat.simplex.common.model.ChatController
import java.io.*
import java.net.URL
import java.nio.file.Files
import java.util.*

actual val appPlatform = AppPlatform.DESKTOP

@Suppress("ConstantLocale")
val defaultLocale: Locale = Locale.getDefault()

fun initApp() {
  chatInitializedAndStarted = {}
  applyAppLocale()
}

private fun applyAppLocale() {
  val lang = ChatController.appPrefs.appLanguage.get()
  if (lang == null || lang == Locale.getDefault().language) return
  Locale.setDefault(Locale.forLanguageTag(lang))
}

// LALAL
actual val appVersionInfo: Pair<String, String?> = "1.0" to null

actual fun initHaskell(socketName: String) {
  val libName1 = "libapp-lib.so"
  val libName2 = "libsimplex.a"
  val url1: URL = DesktopApp::class.java.getResource("/libs/$libName1")!!
  val url2: URL = DesktopApp::class.java.getResource("/libs/$libName2")!!
  val tmpDir = Files.createTempDirectory("simplex-native-libs").toFile()
  tmpDir.deleteOnExit()
  val nativeLibTmpFile1 = File(tmpDir, libName1)
  val nativeLibTmpFile2 = File(tmpDir, libName2)
  nativeLibTmpFile1.deleteOnExit()
  url1.openStream().use { input -> Files.copy(input, nativeLibTmpFile1.toPath()) }
  nativeLibTmpFile2.deleteOnExit()
  url2.openStream().use { input -> Files.copy(input, nativeLibTmpFile2.toPath()) }
  System.load(nativeLibTmpFile2.absolutePath)
  System.load(nativeLibTmpFile1.absolutePath)
  initHS()
}