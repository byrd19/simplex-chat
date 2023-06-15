package chat.simplex.common.platform

import androidx.compose.ui.graphics.*
import boofcv.io.image.ConvertBufferedImage
import boofcv.struct.image.GrayU8
import org.jetbrains.skia.Image
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI
import com.icerockdev.library.MR

actual fun base64ToBitmap(base64ImageString: String): ImageBitmap = TODO()
actual fun resizeImageToStrSize(image: ImageBitmap, maxDataSize: Long): String = TODO()
actual fun resizeImageToDataSize(image: ImageBitmap, usePng: Boolean, maxDataSize: Long): ByteArrayOutputStream = TODO()
actual fun cropToSquare(image: ImageBitmap): ImageBitmap = TODO()
actual fun compressImageStr(bitmap: ImageBitmap): String = TODO()
actual fun compressImageData(bitmap: ImageBitmap, usePng: Boolean): ByteArrayOutputStream = TODO()

actual fun GrayU8.toImageBitmap(): ImageBitmap = ConvertBufferedImage.extractBuffered(this).toComposeImageBitmap()

actual fun ImageBitmap.addLogo(): ImageBitmap {
  val radius = (width * 0.16f).toInt()
  val logoSize = (width * 0.24).toInt()
  val logo: BufferedImage = MR.images.icon_foreground_common.image
  val original = toAwtImage()
  val withLogo = BufferedImage(width, height, original.type)
  val g = withLogo.createGraphics()
  g.setRenderingHint(
    RenderingHints.KEY_INTERPOLATION,
    RenderingHints.VALUE_INTERPOLATION_BILINEAR
  )
  g.drawImage(original, 0, 0, width, height, 0, 0, original.width, original.height, null)
  g.fillRoundRect(width / 2 - radius / 2, height / 2 - radius / 2, radius, radius, radius, radius)
  g.drawImage(logo, (width - logoSize) / 2, (height - logoSize) / 2, logoSize, logoSize, null)
  g.dispose()

  return withLogo.toComposeImageBitmap()
  /*val paint = android.graphics.Paint()
  paint.color = android.graphics.Color.WHITE
  drawCircle(width / 2f, height / 2f, radius, paint)
  val logo = androidAppContext.resources.getDrawable(R.drawable.icon_foreground_common, null).toBitmap()
  val logoSize = (width * 0.24).toInt()
  translate((width - logoSize) / 2f, (height - logoSize) / 2f)
  drawBitmap(logo, null, android.graphics.Rect(0, 0, logoSize, logoSize), null)*/
}

actual fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap {
  val original = toAwtImage()
  val resized = BufferedImage(width, height, original.type)
  val g = resized.createGraphics()
  g.setRenderingHint(
    RenderingHints.KEY_INTERPOLATION,
    RenderingHints.VALUE_INTERPOLATION_BILINEAR
  )
  g.drawImage(original, 0, 0, width, height, 0, 0, original.width, original.height, null)
  g.dispose()
  return resized.toComposeImageBitmap()
}

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