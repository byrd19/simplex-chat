package chat.simplex.common.platform

expect fun copyText(text: String)
expect fun sendEmail(subject: String, body: CharSequence)

expect fun shareText(text: String)
expect fun shareFile(text: String, filePath: String)