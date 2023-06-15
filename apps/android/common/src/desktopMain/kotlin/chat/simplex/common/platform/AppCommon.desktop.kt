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
  val libName1 = "libapp-lib.so"
  val libName2 = "libHSsimplex-chat-5.1.0.1-inplace-ghc8.10.7.so"//"libsimplex.a"
  val url1: URL = DesktopApp::class.java.getResource("/libs/$libName1")!!
  val url2: URL = DesktopApp::class.java.getResource("/libs/$libName2")!!
  val tmpDir = Files.createTempDirectory("simplex-native-libs").toFile()
  // LALAL DELETION DOESN'T WORK
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