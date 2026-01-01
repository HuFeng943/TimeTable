package com.hufeng943.timetable.presentation.theme

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.dynamicColorScheme

// 在你的 Theme.kt 里
@Composable
fun TimeTableTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // 1. 获取动态配色方案（如果系统不支持则返回 null）
    val dynamicColorScheme = remember(context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // 动态色彩从 Android 12 开始支持
            dynamicColorScheme(context)
        } else {
            null
        }
    }

    // 2. 设置回退配色（当动态取色不可用时使用你的品牌色）
    val fallbackColorScheme = ColorScheme(
        primary = Color(0xFFD0BCFF),
        surfaceContainer = Color(0xFF202124),
        // ... 其他颜色定义
    )

    MaterialTheme(
        colorScheme = dynamicColorScheme ?: fallbackColorScheme,
        content = content
    )
}