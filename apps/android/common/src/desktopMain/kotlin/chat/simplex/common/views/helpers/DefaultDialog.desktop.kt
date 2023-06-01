package chat.simplex.common.views.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeDialog
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.Dialog

@Composable
actual fun DefaultDialog(
  onDismissRequest: () -> Unit,
  content: @Composable () -> Unit
) {
  Dialog(
    create = { ComposeDialog() },
    dispose = {},
    onKeyEvent = { event ->
      if (event.key == Key.Escape) {
        onDismissRequest(); true
      } else false
    }
  ) {
    content()
  }
}