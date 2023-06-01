package chat.simplex.common.views.helpers

import chat.simplex.common.model.ChatModel
import chat.simplex.common.views.usersettings.LAMode
import com.icerockdev.library.MR

sealed class LAResult {
  object Success: LAResult()
  class Error(val errString: CharSequence): LAResult()
  class Failed(val errString: CharSequence? = null): LAResult()
  class Unavailable(val errString: CharSequence? = null): LAResult()
}

data class LocalAuthRequest (
  val title: String?,
  val reason: String,
  val password: String,
  val selfDestruct: Boolean,
  val completed: (LAResult) -> Unit
) {
  companion object {
    val sample = LocalAuthRequest(generalGetString(MR.strings.la_enter_app_passcode), generalGetString(MR.strings.la_authenticate), "", selfDestruct = false) { }
  }
}

expect fun authenticate(
  promptTitle: String,
  promptSubtitle: String,
  selfDestruct: Boolean = false,
  usingLAMode: LAMode = ChatModel.controller.appPrefs.laMode.get(),
  completed: (LAResult) -> Unit
)