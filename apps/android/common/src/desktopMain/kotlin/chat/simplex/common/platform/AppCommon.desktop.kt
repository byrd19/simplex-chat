package chat.simplex.common.platform

import chat.simplex.common.DesktopApp
import chat.simplex.common.model.*
import chat.simplex.common.views.call.RcvCallInvitation
import chat.simplex.common.views.helpers.withBGApi
import java.io.*
import java.net.URL
import java.nio.file.Files
import java.util.*

actual val appPlatform = AppPlatform.DESKTOP

@Suppress("ConstantLocale")
val defaultLocale: Locale = Locale.getDefault()

fun initApp() {
  ntfManager = object : NtfManager() { // LALAL
    override fun notifyContactConnected(user: User, contact: Contact) {}
    override fun notifyContactRequestReceived(user: User, cInfo: ChatInfo.ContactRequest) {}
    override fun notifyMessageReceived(user: User, cInfo: ChatInfo, cItem: ChatItem) {}
    override fun notifyCallInvitation(invitation: RcvCallInvitation) {}
    override fun hasNotificationsForChat(chatId: String): Boolean = false
    override fun cancelNotificationsForChat(chatId: String) {}
    override fun displayNotification(user: User, chatId: String, displayName: String, msgText: String, image: String?, actions: List<NotificationAction>) {}
    override fun createNtfChannelsMaybeShowAlert() {}
    override fun cancelCallNotification() {}
    override fun cancelAllNotifications() {}
  }
  chatInitializedAndStarted = {}
  applyAppLocale()
  withBGApi {
    initChatController()
    //LALAL runMigrations()
  }
}

private fun applyAppLocale() {
  val lang = ChatController.appPrefs.appLanguage.get()
  if (lang == null || lang == Locale.getDefault().language) return
  Locale.setDefault(Locale.forLanguageTag(lang))
}

// LALAL
actual val appVersionInfo: Pair<String, String?> = "1.0" to null

actual fun initHaskell(socketName: String) {
  val libName1 = "libapp-lib.${platform.libExtension}"
  val libName2 = "libHSsimplex-chat-5.1.0.1-inplace-ghc8.10.7.${platform.libExtension}"//"libsimplex.a"
  val url1: URL = DesktopApp::class.java.getResource("${platform.libPath}/$libName1")!!
  val url2: URL = DesktopApp::class.java.getResource("${platform.libPath}/$libName2")!!
  val tmpDir = Files.createTempDirectory("simplex-native-libs").toFile()
  tmpDir.deleteOnExit()
  val nativeLibTmpFile1 = File(tmpDir, libName1)
  val nativeLibTmpFile2 = File(tmpDir, libName2)
  nativeLibTmpFile1.deleteOnExit()
  url1.openStream().use { input -> Files.copy(input, nativeLibTmpFile1.toPath()) }
  nativeLibTmpFile2.deleteOnExit()
  url2.openStream().use { input -> Files.copy(input, nativeLibTmpFile2.toPath()) }
  System.load(nativeLibTmpFile1.absolutePath)
  //System.load(nativeLibTmpFile2.absolutePath)
  initHS()
}

private val home = System.getProperty("user.home")
val platform = detectPlatform()

enum class Platform(val libPath: String, val libExtension: String, val configPath: String) {
  LINUX_X86_64("/libs/linux-x86_64", "so", "$home/.config/simplex"),
  LINUX_AARCH64("/libs/aarch64", "so", "$home/.config/simplex"),
  WINDOWS_X86_64("/libs/windows-x86_64", "dll", System.getenv("AppData") + File.separator + "simplex"),
  MAC_X86_64("/libs/mac-x86_64", "dylib", "$home/.config/simplex"),
  MAC_AARCH64("/libs/mac-aarch64", "dylib", "$home/.config/simplex");
}

private fun detectPlatform(): Platform {
  val os = System.getProperty("os.name", "generic").lowercase(Locale.ENGLISH)
  val arch = System.getProperty("os.arch")
  return when {
    os == "linux" && (arch.contains("x86") || arch == "amd64") -> Platform.LINUX_X86_64
    os == "linux" && arch == "aarch64" -> Platform.LINUX_AARCH64
    os.contains("windows") && (arch.contains("x86") || arch == "amd64") -> Platform.WINDOWS_X86_64
    os.contains("mac") && arch.contains("x86") -> Platform.MAC_X86_64
    os.contains("mac") && arch.contains("aarch64") -> Platform.MAC_AARCH64
    else -> TODO("Currently, your processor's architecture ($arch) or os ($os) are unsupported. Please, contact us")
  }
}