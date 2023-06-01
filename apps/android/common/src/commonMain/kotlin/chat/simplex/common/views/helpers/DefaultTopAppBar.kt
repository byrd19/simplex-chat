package chat.simplex.common.views.helpers

import com.icerockdev.library.MR
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.unit.dp
import chat.simplex.common.ui.theme.*

@Composable
fun DefaultTopAppBar(
  navigationButton: @Composable RowScope.() -> Unit,
  title: (@Composable () -> Unit)?,
  onTitleClick: (() -> Unit)? = null,
  showSearch: Boolean,
  onSearchValueChanged: (String) -> Unit,
  buttons: List<@Composable RowScope.() -> Unit> = emptyList(),
) {
  // If I just disable clickable modifier when don't need it, it will stop passing clicks to search. Replacing the whole modifier
  val modifier = if (!showSearch) {
    Modifier.clickable(enabled = onTitleClick != null, onClick = onTitleClick ?: { })
  } else Modifier

  TopAppBar(
    modifier = modifier,
    title = {
      if (!showSearch) {
        title?.invoke()
      } else {
        SearchTextField(Modifier.fillMaxWidth(), stringResource(MR.strings.search), alwaysVisible = false, onSearchValueChanged)
      }
    },
    backgroundColor = if (isInDarkTheme()) ToolbarDark else ToolbarLight,
    navigationIcon = navigationButton,
    buttons = if (!showSearch) buttons else emptyList(),
    centered = !showSearch,
  )
}

@Composable
fun NavigationButtonBack(onButtonClicked: (() -> Unit)?) {
  IconButton(onButtonClicked ?: {}, enabled = onButtonClicked != null) {
    Icon(
      painterResource(MR.images.ic_arrow_back_ios_new), stringResource(MR.strings.back), tint = if (onButtonClicked != null) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
    )
  }
}

@Composable
fun ShareButton(onButtonClicked: () -> Unit) {
  IconButton(onButtonClicked) {
    Icon(
      painterResource(MR.images.ic_share), stringResource(MR.strings.share_verb), tint = MaterialTheme.colors.primary
    )
  }
}

@Composable
fun NavigationButtonMenu(onButtonClicked: () -> Unit) {
  IconButton(onClick = onButtonClicked) {
    Icon(
      painterResource(MR.images.ic_menu),
      stringResource(MR.strings.icon_descr_settings),
      tint = MaterialTheme.colors.primary,
    )
  }
}

@Composable
private fun TopAppBar(
  title: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable (RowScope.() -> Unit)? = null,
  buttons: List<@Composable RowScope.() -> Unit> = emptyList(),
  backgroundColor: Color = MaterialTheme.colors.primarySurface,
  centered: Boolean,
) {
  Box(
    modifier
      .fillMaxWidth()
      .height(AppBarHeight)
      .background(backgroundColor)
      .padding(horizontal = 4.dp),
    contentAlignment = Alignment.CenterStart,
  ) {
    if (navigationIcon != null) {
      Row(
        Modifier
          .fillMaxHeight()
          .width(TitleInsetWithIcon - AppBarHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
        content = navigationIcon
      )
    }
    Row(
      Modifier
        .fillMaxHeight()
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.End,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      buttons.forEach { it() }
    }
    val startPadding = if (navigationIcon != null) TitleInsetWithIcon else TitleInsetWithoutIcon
    val endPadding = (buttons.size * 50f).dp
    Box(
      Modifier
        .fillMaxWidth()
        .padding(
          start = if (centered) kotlin.math.max(startPadding.value, endPadding.value).dp else startPadding,
          end = if (centered) kotlin.math.max(startPadding.value, endPadding.value).dp else endPadding
        ),
      contentAlignment = Alignment.Center
    ) {
      title()
    }
  }
}

val AppBarHeight = 56.dp
val AppBarHorizontalPadding = 4.dp
private val TitleInsetWithoutIcon = DEFAULT_PADDING - AppBarHorizontalPadding
val TitleInsetWithIcon = 72.dp
