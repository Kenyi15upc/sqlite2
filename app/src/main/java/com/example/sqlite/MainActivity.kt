package com.example.sqlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.sqlite.data.local.DatabaseHelper
import com.example.sqlite.data.local.PreferencesManager
import com.example.sqlite.ui.screens.HomeScreen
import com.example.sqlite.ui.theme.SqliteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SqliteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val dbHelper = remember { DatabaseHelper(context) }
                    val preferencesManager = remember { PreferencesManager(context) }

                    HomeScreen(
                        dbHelper = dbHelper,
                        preferencesManager = preferencesManager
                    )
                }
            }
        }
    }
}
