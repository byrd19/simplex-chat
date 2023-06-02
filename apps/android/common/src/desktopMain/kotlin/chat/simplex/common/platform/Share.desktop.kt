package chat.simplex.common.platform

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import java.io.File
import java.net.URI
import java.net.URLEncoder

actual fun UriHandler.sendEmail(subject: String, body: CharSequence) {
  val subjectEncoded = URLEncoder.encode(subject, "UTF-8").replace("+", "%20")
  val bodyEncoded = URLEncoder.encode(subject, "UTF-8").replace("+", "%20")
  openUri("mailto:?subject=$subjectEncoded&body=$bodyEncoded")
}

actual fun ClipboardManager.shareText(text: String) {
  setText(AnnotatedString(text))
}

actual fun shareFile(text: String, filePath: String) {
  FileChooserLauncher { to: URI? ->
    if (to != null) {
      copyFileToFile(File(filePath), to) {}
    }
  }.launch(filePath)
}