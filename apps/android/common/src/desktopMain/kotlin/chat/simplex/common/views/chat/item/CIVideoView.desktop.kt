package chat.simplex.common.views.chat.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import chat.simplex.common.platform.VideoPlayer

@Composable
actual fun PlayerView(player: VideoPlayer, width: Dp, onClick: () -> Unit, onLongClick: () -> Unit, stop: () -> Unit) {
  TODO()
}

@Composable
actual fun LocalWindowWidth(): Dp =
  with(LocalDensity.current) { (java.awt.Window.getWindows().find { it.isActive }?.width ?: 0).toDp() }