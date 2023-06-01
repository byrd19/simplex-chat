package chat.simplex.common.platform

import chat.simplex.common.model.ChatController
import java.io.*
import java.net.ServerSocket
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread
import kotlin.random.Random

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
actual val appVersionInfo: String = "1.0"

// LALAL make with port, not socket name
actual fun initHaskell(socketName: String) {
  val s = Semaphore(0)
  thread(name="stdout/stderr pipe") {
    Log.d(TAG, "starting server")
    var server: ServerSocket? = null
    val port = Random.nextInt(1024, 65535 - 100)
    for (i in 0..100) {
      try {
        server = ServerSocket(port + i)
        break
      } catch (e: IOException) {
        Log.e(TAG, e.stackTraceToString())
      }
    }
    if (server == null) {
      throw Error("Unable to setup local server socket. Contact developers")
    }
    Log.d(TAG, "started server")
    s.release()
    val receiver = server.accept()
    Log.d(TAG, "started receiver")
    val logbuffer = FifoQueue<String>(500)
    if (receiver != null) {
      val inStream = receiver.inputStream
      val inStreamReader = InputStreamReader(inStream)
      val input = BufferedReader(inStreamReader)
      Log.d(TAG, "starting receiver loop")
      while (true) {
        val line = input.readLine() ?: break
        Log.w("$TAG (stdout/stderr)", line)
        logbuffer.add(line)
      }
      Log.w(TAG, "exited receiver loop")
    }
  }

  System.loadLibrary("app-lib")

  s.acquire()
  pipeStdOutToSocket(socketName)

  initHS()
}