package chat.simplex.common.platform

import java.io.*
import java.net.ServerSocket
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

enum class AppPlatform {
  ANDROID, DESKTOP
}

expect val appPlatform: AppPlatform

expect val appVersionInfo: String

expect fun initHaskell(socketName: String)

class FifoQueue<E>(private var capacity: Int) : LinkedList<E>() {
  override fun add(element: E): Boolean {
    if(size > capacity) removeFirst()
    return super.add(element)
  }
}