package chat.simplex.common.views.newchat

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.unit.sp
import com.icerockdev.library.MR
import chat.simplex.common.model.ChatModel

enum class ConnectViaLinkTab {
  SCAN, PASTE
}

@Composable
fun ConnectViaLinkView(m: ChatModel, close: () -> Unit) {
  val selection = remember {
    mutableStateOf(
      runCatching { ConnectViaLinkTab.valueOf(m.controller.appPrefs.connectViaLinkTab.get()!!) }.getOrDefault(ConnectViaLinkTab.SCAN)
    )
  }
  val tabTitles = ConnectViaLinkTab.values().map {
    when (it) {
      ConnectViaLinkTab.SCAN -> stringResource(MR.strings.scan_QR_code)
      ConnectViaLinkTab.PASTE -> stringResource(MR.strings.paste_the_link_you_received)
    }
  }
  Column(
    Modifier.fillMaxHeight(),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(Modifier.weight(1f)) {
      when (selection.value) {
        ConnectViaLinkTab.SCAN -> {
          ScanToConnectView(m, close)
        }
        ConnectViaLinkTab.PASTE -> {
          PasteToConnectView(m, close)
        }
      }
    }
    TabRow(
      selectedTabIndex = selection.value.ordinal,
      backgroundColor = Color.Transparent,
      contentColor = MaterialTheme.colors.primary,
    ) {
      tabTitles.forEachIndexed { index, it ->
        Tab(
          selected = selection.value.ordinal == index,
          onClick = {
            selection.value = ConnectViaLinkTab.values()[index]
            m.controller.appPrefs.connectViaLinkTab.set(selection.value .name)
          },
          text = { Text(it, fontSize = 13.sp) },
          icon = {
            Icon(
              if (ConnectViaLinkTab.SCAN.ordinal == index) painterResource(MR.images.ic_qr_code) else painterResource(MR.images.ic_article),
              it
            )
          },
          selectedContentColor = MaterialTheme.colors.primary,
          unselectedContentColor = MaterialTheme.colors.secondary,
        )
      }
    }
  }
}
