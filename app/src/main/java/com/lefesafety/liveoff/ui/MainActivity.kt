package com.lefesafety.liveoff.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lefesafety.liveoff.ui.navigation.LiveOffNavGraph
import com.lefesafety.liveoff.ui.navigation.Screen
import com.lefesafety.liveoff.ui.theme.AccentSosRed
import com.lefesafety.liveoff.ui.theme.LiveOffTheme
import com.lefesafety.liveoff.ui.theme.TextPrimary
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveOffTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                val showSosFab = currentRoute != null && !currentRoute.contains("Sos")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        if (showSosFab) {
                            FloatingActionButton(
                                onClick = { navController.navigate(Screen.Sos) },
                                containerColor = AccentSosRed,
                                contentColor = TextPrimary
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = "SOS")
                            }
                        }
                    }
                ) { innerPadding ->
                    LiveOffNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
