package com.example.smarthome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.collectAsState
//import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smarthome.ui.components.SmartHomeBottomBar
import com.example.smarthome.ui.screens.FavoritesScreen
import com.example.smarthome.ui.screens.ThingsScreen
import com.example.smarthome.ui.screens.SettingsScreen
import com.example.smarthome.ui.screens.SettingsViewModel
import com.example.smarthome.ui.screens.SettingsViewModelFactory
import com.example.smarthome.ui.theme.SmartHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //change on the name of the theme
            SmartHomeTheme {
                SmartHomeApp()
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
//@Composable
//fun DynamicSmartHomeTheme(content: @Composable () -> Unit) {
//    // Get the current context
//    val context = LocalContext.current
//
//    // Get the settings view model
//    val viewModel: SettingsViewModel = viewModel(
//        factory = SettingsViewModelFactory(context)
//    )
//
//    // Get the saved app color
//    val appColorInt by viewModel.appColor.collectAsState(initial = Color(0xFFFFD500).toArgb()) // Default yellow
//    val primaryColor = remember(appColorInt) { Color(appColorInt) }
//
//    // Create a dynamic color scheme based on the user's selection
//    val colorScheme = lightColorScheme(
//        primary = primaryColor,
//        primaryContainer = primaryColor.copy(alpha = 0.1f),
//        secondary = primaryColor.copy(alpha = 0.7f),
//        // Set other colors as needed, or let the default values handle them
//    )
//
//}

@Composable
fun SmartHomeApp() {
    val navController = rememberNavController()
    //change made until val items
    val context = LocalContext.current

    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context)
    )

    val items = listOf(
        Screen.Favorites,
        Screen.Things,
        Screen.Routines,
        Screen.Ideas,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            SmartHomeBottomBar(
                items = items,
                navController = navController
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Favorites.route
            ) {
                composable(Screen.Favorites.route) {
                    FavoritesScreen()
                }
                composable(Screen.Things.route) {
                    ThingsScreen()
                }
                composable(Screen.Routines.route) {
                    RoutinesScreen()
                }
                composable(Screen.Ideas.route) {
                    // Placeholder for ideas
                }
                composable(Screen.Settings.route) {
                    // Added implementation for Settings screen
                    SettingsScreen()
//                        appThemeColor = MaterialTheme.colorScheme.primary,
//                        onColorChanged = { /* Color changes are handled via DataStore */ }

                }
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: Int) {
    data object Favorites : Screen("favorites", "Favorites", R.drawable.ic_star)
    data object Things : Screen("things", "Things", R.drawable.ic_things)
    data object Routines : Screen("routines", "Routines", R.drawable.ic_routines)
    data object Ideas : Screen("ideas", "Ideas", R.drawable.ic_ideas)
    data object Settings : Screen("settings", "Settings", R.drawable.ic_settings)
}