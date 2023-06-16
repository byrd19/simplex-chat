package chat.simplex.common.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.russhwolf.settings.*
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc
import java.io.File
import java.util.*

@Composable
actual fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font =
  androidx.compose.ui.text.platform.Font("MR/fonts/$res.ttf", weight, style)

actual fun StringResource.localized(): String = desc().toString()

actual fun isInNightMode() = false

private val settingsFile =
  File(platform.configPath + File.separator + "settings.properties")
    .also { it.parentFile.mkdirs() }
private val settingsThemesFile =
  File(platform.configPath + File.separator + "themes.properties")
    .also { it.parentFile.mkdirs() }
private val settingsProps =
  Properties()
    .also { try { it.load(settingsFile.reader()) } catch (e: Exception) { Properties() } }
private val settingsThemesProps =
  Properties()
    .also { try { it.load(settingsThemesFile.reader()) } catch (e: Exception) { Properties() } }

actual val settings: Settings = PropertiesSettings(settingsProps) { settingsProps.store(settingsFile.writer(), "") }
actual val settingsThemes: Settings = PropertiesSettings(settingsThemesProps) { settingsThemesProps.store(settingsThemesFile.writer(), "") }

actual fun screenOrientation(): ScreenOrientation = ScreenOrientation.UNDEFINED

@Composable // LALAL
actual fun screenWidthDp(): Int = java.awt.Toolkit.getDefaultToolkit().screenSize.width.also { println("LALAL $it") }// LALAL java.awt.Desktop.getDesktop()

actual fun isRtl(text: CharSequence): Boolean = false // LALAL