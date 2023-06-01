package chat.simplex.common.platform

import androidx.compose.ui.graphics.*
import boofcv.io.image.ConvertBufferedImage
import boofcv.struct.image.GrayU8
import org.jetbrains.skia.Image
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI

actual fun base64ToBitmap(base64ImageString: String): ImageBitmap = TODO()
actual fun resizeImageToStrSize(image: ImageBitmap, maxDataSize: Long): String = TODO()
actual fun resizeImageToDataSize(image: ImageBitmap, usePng: Boolean, maxDataSize: Long): ByteArrayOutputStream = TODO()
actual fun cropToSquare(image: ImageBitmap): ImageBitmap = TODO()
actual fun compressImageStr(bitmap: ImageBitmap): String = TODO()
actual fun compressImageData(bitmap: ImageBitmap, usePng: Boolean): ByteArrayOutputStream = TODO()

actual fun GrayU8.toImageBitmap(): ImageBitmap = ConvertBufferedImage.extractBuffered(this).toComposeImageBitmap()
actual fun ImageBitmap.addLogo(): ImageBitmap = TODO()
actual fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap = TODO()

// LALAL
actual fun isImage(uri: URI): Boolean {
  val path = uri.path.lowercase()
  return path.endsWith(".gif") ||
      path.endsWith(".webp") ||
      path.endsWith(".png") ||
      path.endsWith(".jpg") ||
      path.endsWith(".jpeg")
}

actual fun isAnimImage(uri: URI, drawable: Any?): Boolean {
  val path = uri.path.lowercase()
  return path.endsWith(".gif") || path.endsWith(".webp")
}

@Suppress("NewApi")
actual fun loadImageBitmap(inputStream: InputStream): ImageBitmap =
  Image.makeFromEncoded(inputStream.readAllBytes()).toComposeImageBitmap()