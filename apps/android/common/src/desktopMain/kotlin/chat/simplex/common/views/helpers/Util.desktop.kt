package chat.simplex.common.views.helpers

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Density
import chat.simplex.common.model.CIFile
import chat.simplex.common.platform.VideoPlayerInterface
import java.io.File
import java.net.URI

actual fun spannableStringToAnnotatedString(
  text: CharSequence,
  density: Density,
): AnnotatedString {
  TODO()
}

actual fun getLoadedImage(file: CIFile?): ImageBitmap? {
  TODO()
}

actual fun getFileName(uri: URI): String? = TODO()

actual fun getAppFilePath(uri: URI): String? = TODO()

actual fun getFileSize(uri: URI): Long? = TODO()

actual fun getBitmapFromUri(uri: URI, withAlertOnException: Boolean): ImageBitmap? = TODO()

actual fun getDrawableFromUri(uri: URI, withAlertOnException: Boolean): Any? = TODO()

actual fun saveTempImageUncompressed(image: ImageBitmap, asPng: Boolean): File? = TODO()

actual fun getBitmapFromVideo(uri: URI, timestamp: Long?, random: Boolean): VideoPlayerInterface.PreviewAndDuration {
  TODO()
}