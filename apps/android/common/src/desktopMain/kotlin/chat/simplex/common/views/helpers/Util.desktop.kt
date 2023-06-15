package chat.simplex.common.views.helpers

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Density
import chat.simplex.common.model.CIFile
import chat.simplex.common.model.ChatModel
import chat.simplex.common.platform.*
import chat.simplex.common.simplexWindowState
import org.jetbrains.skiko.toBitmap
import java.awt.Desktop
import java.awt.FileDialog
import java.io.File
import java.net.URI
import javax.imageio.ImageIO
import javax.swing.JFileChooser

actual fun spannableStringToAnnotatedString(
  text: CharSequence,
  density: Density,
): AnnotatedString {
  // LALAL
  return AnnotatedString(text.toString())
}

actual fun getLoadedImage(file: CIFile?): ImageBitmap? {
  TODO()
}

actual fun getFileName(uri: URI): String? = TODO()

actual fun getAppFilePath(uri: URI): String? = TODO()

actual fun getFileSize(uri: URI): Long? = TODO()

actual fun getBitmapFromUri(uri: URI, withAlertOnException: Boolean): ImageBitmap? = TODO()

actual fun getDrawableFromUri(uri: URI, withAlertOnException: Boolean): Any? = TODO()

actual suspend fun saveTempImageUncompressed(image: ImageBitmap, asPng: Boolean): File? {
  val file = simplexWindowState.saveDialog.awaitResult()
  return if (file != null) {
    try {
      val ext = if (asPng) "png" else "jpg"
      val newFile = File(file.absolutePath + File.separator + generateNewFileName("IMG", ext))
      ImageIO.write(image.toAwtImage(), ext.uppercase(), newFile)
      newFile
    } catch (e: Exception) {
      Log.e(TAG, "Util.kt saveTempImageUncompressed error: ${e.message}")
      null
    }
  } else null
}

actual fun getBitmapFromVideo(uri: URI, timestamp: Long?, random: Boolean): VideoPlayerInterface.PreviewAndDuration {
  TODO()
}