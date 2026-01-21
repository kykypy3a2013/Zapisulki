package com.example.zapisulki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.zapisulki.ui.navigation.AppNavigation
import com.example.zapisulki.ui.theme.MyAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigationWithSplash()
                }
            }
        }
    }
}

@Composable
fun AppNavigationWithSplash() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        LaunchedEffect(Unit) {
            delay(300) // Минимальная задержка для инициализации
            showSplash = false
        }
    } else {
        AppNavigation()
    }
}