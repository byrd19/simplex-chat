package chat.simplex.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import chat.simplex.common.ui.theme.SimpleXTheme
import chat.simplex.common.views.helpers.FileDialogChooser
import kotlinx.coroutines.*
import java.io.File

val simplexWindowState = SimplexWindowState()

fun showApp() = application {
  Window(onCloseRequest = ::exitApplication, title = "SimpleX") {
    SimpleXTheme {
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
      val toasts = remember { simplexWindowState.toasts }
      val toast = toasts.firstOrNull()
      if (toast != null) {
        Box(Modifier.fillMaxSize().padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter) {
          Text(
            toast.first,
            Modifier.background(MaterialTheme.colors.primary, RoundedCornerShape(100)).padding(vertical = 5.dp, horizontal = 10.dp),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1
          )
        }
        // Shows toast in insertion order with preferred delay per toast. New one will be shown once previous one expires
        LaunchedEffect(toast, toasts.size) {
          delay(toast.second)
          simplexWindowState.toasts.removeFirst()
        }
      }
    }
  }
}

class SimplexWindowState {
  val openDialog = DialogState<File?>()
  val saveDialog = DialogState<File?>()
  val toasts = mutableStateListOf<Pair<String, Long>>()
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
  SimpleXTheme {
    App()
  }
}

/** Needed for [chat.simplex.common.platform.Files] to get path to jar file */
class DesktopApp()