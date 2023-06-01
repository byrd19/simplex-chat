package chat.simplex.common.platform

import android.annotation.SuppressLint
import android.content.Context
import android.net.LocalServerSocket
import androidx.fragment.app.FragmentActivity
import chat.simplex.common.BuildConfig
import java.io.*
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

actual val appPlatform = AppPlatform.ANDROID

var isAppOnForeground: Boolean = false

@Suppress("ConstantLocale")
val defaultLocale: Locale = Locale.getDefault()

@SuppressLint("StaticFieldLeak")
lateinit var androidAppContext: Context
lateinit var mainActivity: WeakReference<FragmentActivity>

lateinit var serviceStart: suspend () -> Unit
lateinit var serviceSafeStop: () -> Unit

actual val appVersionInfo = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"


actual fun initHaskell(socketName: String) {
  val s = Semaphore(0)
  thread(name="stdout/stderr pipe") {
    Log.d(TAG, "starting server")
    var server: LocalServerSocket? = null
    for (i in 0..100) {
      try {
        server = LocalServerSocket(socketName + i)
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