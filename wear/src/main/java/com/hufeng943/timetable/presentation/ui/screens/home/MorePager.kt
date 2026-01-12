package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.presentation.ui.NavRoutes

@Composable
fun MorePager() {
    val scrollState = rememberScalingLazyListState()
    val navController = LocalNavController.current

    val menuItems = listOf(
        MoreMenuItemUi(stringResource(R.string.more_menu_edit), Icons.Default.Edit) {
            navController.navigate(NavRoutes.EDIT_COURSE)
        },
        MoreMenuItemUi(
            stringResource(R.string.more_menu_settings),
            Icons.Default.Settings
        ) { TODO() },
        MoreMenuItemUi(stringResource(R.string.more_menu_about), Icons.Default.Info) { TODO() },
    )

    ScreenScaffold(scrollState = scrollState) {
        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.more_title))
                }
            }

            items(menuItems) { item ->
                Button(onClick = item.onClick, modifier = Modifier.fillMaxWidth(), icon = {
                    Icon(
                        imageVector = item.icon, contentDescription = null
                    )
                }, label = { Text(item.label) })
            }
        }
    }
}

internal data class MoreMenuItemUi(
    val label: String, val icon: ImageVector, val onClick: () -> Unit
)
