package chat.simplex.common.views.chat.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import chat.simplex.common.model.CIFile
import java.net.URI

@Composable
actual fun SimpleAndAnimatedImageView(
  uri: URI,
  imageBitmap: ImageBitmap,
  file: CIFile?,
  imageProvider: () -> ImageGalleryProvider,
  ImageView: @Composable (painter: Painter, onClick: () -> Unit) -> Unit
) {
  TODO()
}