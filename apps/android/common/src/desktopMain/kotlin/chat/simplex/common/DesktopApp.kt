package chat.simplex.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import chat.simplex.common.views.helpers.FileDialogChooser
import kotlinx.coroutines.CompletableDeferred
import java.io.File

val simplexWindowState = SimplexWindowState()

fun showApp() = application {
  Window(onCloseRequest = ::exitApplication, title = "SimpleX") {
    App()
    if (simplexWindowState.openDialog.isAwaiting) {
      FileDialogChooser(
        title = "SimpleX",
        isLoad = true,
        onResult = {
          simplexWindowState.openDialog.onResult(it)
        }
      )
    }

    if (simplexWindowState.saveDialog.isAwaiting) {
      FileDialogChooser(
        title = "SimpleX",
        isLoad = false,
        onResult = { simplexWindowState.saveDialog.onResult(it) }
      )
    }
  }
}

class SimplexWindowState {
  val openDialog = DialogState<File?>()
  val saveDialog = DialogState<File?>()
}

class DialogState<T> {
  private var onResult: CompletableDeferred<T>? by mutableStateOf(null)

  val isAwaiting get() = onResult != null

  suspend fun awaitResult(): T {
    onResult = CompletableDeferred()
    val result = onResult!!.await()
    onResult = null
    return result
  }

  fun onResult(result: T) = onResult!!.complete(result)
}

@Preview
@Composable
fun AppPreview() {
  App()
}

/** Needed for [chat.simplex.common.platform.Files] to get path to jar file */
class DesktopApp()