package chat.simplex.common.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.russhwolf.settings.*
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc
import java.util.*

@Composable
actual fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font =
  androidx.compose.ui.text.platform.Font("MR/fonts/$res.ttf", weight, style)

actual fun StringResource.localized(): String = desc().toString()

actual fun isInNightMode() = false

// LALAL
actual val settings: Settings = PropertiesSettings(Properties())
actual val settingsThemes: Settings = PropertiesSettings(Properties())

actual fun screenOrientation(): ScreenOrientation = ScreenOrientation.UNDEFINED

@Composable // LALAL
actual fun screenWidthDp(): Int = java.awt.Toolkit.getDefaultToolkit().screenSize.width.also { println("LALAL $it") }// LALAL java.awt.Desktop.getDesktop()

actual fun isRtl(text: CharSequence): Boolean = false // LALAL