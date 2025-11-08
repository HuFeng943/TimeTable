package com.hufeng943.timetable.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.profileinstaller.ProfileInstaller
import com.hufeng943.timetable.presentation.ui.MainContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO)
        {
            try {
                ProfileInstaller.writeProfile(this@MainActivity)
                Log.d("ProfileInstaller", "AOT profile 写入成功")
            } catch (e: Exception) {
                Log.w("ProfileInstaller", "AOT 写入失败: ${e.message}", e)
            }
        } // 给没Google Play的设备跑跑 AOT

        setContent {
            MainContent()
        }
    }
}