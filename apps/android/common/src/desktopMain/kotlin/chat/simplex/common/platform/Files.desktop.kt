package chat.simplex.common.platform

import androidx.compose.runtime.*
import chat.simplex.common.DesktopApp
import java.io.*
import java.net.URI

private fun applicationParentPath(): String = try {
  DesktopApp::class.java.protectionDomain!!.codeSource.location.toURI().path
    .replaceAfterLast("/", "")
    .replaceAfterLast(File.separator, "")
    .replace("/", File.separator)
} catch (e: Exception) {
  "./"
}

// LALAL
actual val dataDir: File = File(applicationParentPath())
actual val tmpDir: File = File(System.getProperty("java.io.tmpdir"))
actual val cacheDir: File = tmpDir

@Composable
actual fun rememberFileChooserLauncher(getContent: Boolean, onResult: (URI?) -> Unit): FileChooserLauncher =
  remember { FileChooserLauncher(onResult) }

actual class FileChooserLauncher actual constructor() {
  lateinit var onResult: (URI?) -> Unit

  constructor(onResult: (URI?) -> Unit): this() {
    this.onResult = onResult
  }

  actual fun launch(input: String) {
    // LALAL
    onResult(null)
  }
}

actual fun URI.inputStream(): InputStream? = File(this).inputStream()
actual fun URI.outputStream(): OutputStream = File(this).outputStream()
