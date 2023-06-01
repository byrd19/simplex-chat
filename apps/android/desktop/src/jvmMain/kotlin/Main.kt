import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import chat.simplex.common.App
import chat.simplex.common.platform.*
import java.util.*

fun main() {
  initHaskell("$appVersionInfo.local.socket.address.listen.native.cmd2" + System.currentTimeMillis())
  initApp()
  application {
    Window(onCloseRequest = ::exitApplication) {
      App()
    }
  }
}
