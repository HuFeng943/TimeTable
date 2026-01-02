package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text

@Composable
fun MorePager(
    navController: NavHostController
) {
    val scrollState = rememberScalingLazyListState()

    val menuItems = listOf(
        MoreMenuItemUi("编辑课程表", Icons.Default.Edit) {
            navController.navigate("edit_course")
        },
        MoreMenuItemUi("设置中心", Icons.Default.Settings) {TODO()},
        MoreMenuItemUi("关于我们", Icons.Default.Info) {TODO()},
    )
    ScreenScaffold(scrollState = scrollState) {
        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                ListHeader {
                    Text("更多")
                }
            }

            items(menuItems) { item ->
                Button(
                    onClick = item.onClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(item.label)
                }
            }
        }
    }
}

internal data class MoreMenuItemUi(
    val label: String, val icon: ImageVector, val onClick: () -> Unit
)
