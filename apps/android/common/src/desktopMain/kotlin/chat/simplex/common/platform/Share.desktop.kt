package chat.simplex.common.platform

actual fun copyText(text: String) {
  // LALAL
  //LocalClipboardManager.current.setText(AnnotatedString(text))
}

// LALAL escape string
actual fun sendEmail(subject: String, body: CharSequence) {
  // LALAL
  //LocalUriHandler.current.openUri("mailto:?subject=${subject}&body=body")
}

actual fun shareText(text: String) {
  copyText(text)
}

actual fun shareFile(text: String, filePath: String) {

}