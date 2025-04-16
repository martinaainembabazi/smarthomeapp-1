//package com.example.smarthome.ui.theme
//
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Shapes
//import androidx.compose.material3.lightColorScheme
//import androidx.compose.material3.darkColorScheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.staticCompositionLocalOf
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.smarthome.ui.screens.dataStore
//import com.example.smarthome.ui.screens.PreferencesKeys
//import com.example.smarthome.ui.viewModel.ThemeViewModel
//import kotlinx.coroutines.flow.map
//
//// Define theme colors as a sealed class for better type safety
//sealed class ThemeColor(val color: Color, val name: String) {
//    data object Yellow : ThemeColor(Color(0xFFFFD500), "Yellow")
//    data object Blue : ThemeColor(Color(0xFF2196F3), "Blue")
//    data object Green : ThemeColor(Color(0xFF4CAF50), "Green")
//    data object Red : ThemeColor(Color(0xFFF44336), "Red")
//    data object Purple : ThemeColor(Color(0xFF9C27B0), "Purple")
//    data object Orange : ThemeColor(Color(0xFFFF9800), "Orange")
//    data object Brown : ThemeColor(Color(0xFF795548), "Brown")
//    data object Gray : ThemeColor(Color(0xFF607D8B), "Gray")
//
//    companion object {
//        val allColors = listOf(Yellow, Blue, Green, Red, Purple, Orange, Brown, Gray)
//
//        fun fromArgb(colorInt: Int): ThemeColor {
//            return allColors.firstOrNull { it.color.toArgb() == colorInt } ?: Yellow
//        }
//    }
//}
//
//// Create a CompositionLocal for the current theme color
//val LocalThemeColor = staticCompositionLocalOf { ThemeColor.Yellow }
//
//// Dynamic color schemes
//private fun dynamicDarkColorScheme(primary: Color) = darkColorScheme(
//    primary = primary,
//    secondary = primary.copy(alpha = 0.7f),
//    tertiary = primary.copy(alpha = 0.5f),
//    surface = Color(0xFF121212),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    // Add other dark theme specific colors
//)
//
//private fun dynamicLightColorScheme(primary: Color) = lightColorScheme(
//    primary = primary,
//    secondary = primary.copy(alpha = 0.7f),
//    tertiary = primary.copy(alpha = 0.5f),
//    surface = Color.White,
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    // Add other light theme specific colors
//)
//
//// Theme.kt
//@Composable
//fun SmartHomeTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    content: @Composable () -> Unit
//) {
//    val themeViewModel: ThemeViewModel = viewModel()
//    val themeColor by themeViewModel.currentThemeColor.collectAsState()
//
//    val colorScheme = if (darkTheme) {
//        dynamicDarkColorScheme(themeColor.color)
//    } else {
//        dynamicLightColorScheme(themeColor.color)
//    }
//
//    CompositionLocalProvider(LocalThemeColor provides themeColor as ThemeColor.Yellow) {
//        MaterialTheme(
//            colorScheme = colorScheme,
//            typography = Typography,
////            shapes = Shapes,
//            content = content
//        )
//    }
//}
//
//// Extension to get current theme color
//val MaterialTheme.themeColor: ThemeColor
//    @Composable get() = LocalThemeColor.current
package com.example.smarthome.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map
import com.example.smarthome.ui.screens.dataStore
import com.example.smarthome.ui.screens.PreferencesKeys

// Define the same color options as in settings.kt
object ThemeColors {
    val Yellow = Color(0xFFFFD500) // Default
    val Blue = Color(0xFF2196F3)
    val Green = Color(0xFF4CAF50)
    val Red = Color(0xFFF44336)
    val Purple = Color(0xFF9C27B0)
    val Orange = Color(0xFFFF9800)
    val Brown = Color(0xFF795548)
    val Gray = Color(0xFF607D8B)

    // Create a map for easy lookup
    private val colorMap = mapOf(
        Yellow.toArgb() to Yellow,
        Blue.toArgb() to Blue,
        Green.toArgb() to Green,
        Red.toArgb() to Red,
        Purple.toArgb() to Purple,
        Orange.toArgb() to Orange,
        Brown.toArgb() to Brown,
        Gray.toArgb() to Gray
    )

    // Get Color from int (with fallback)
    fun fromArgb(colorInt: Int): Color {
        return colorMap[colorInt] ?: Yellow
    }
}

// Default color schemes with Yellow as default primary
private val DarkColorScheme = darkColorScheme(
    primary = ThemeColors.Yellow,
    secondary = ThemeColors.Yellow.copy(alpha = 0.7f),
    tertiary = ThemeColors.Yellow.copy(alpha = 0.5f)
    // Add other color assignments as needed
)

private val LightColorScheme = lightColorScheme(
    primary = ThemeColors.Yellow,
    secondary = ThemeColors.Yellow.copy(alpha = 0.7f),
    tertiary = ThemeColors.Yellow.copy(alpha = 0.5f)
    // Add other color assignments as needed
)

@Composable
fun SmartHomeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit



) {
    val context = LocalContext.current

    // Get saved app color from DataStore using the same key as in settings.kt
    val colorFlow = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.APP_COLOR] ?: ThemeColors.Yellow.toArgb() // Default yellow
    }

    val savedColorInt by colorFlow.collectAsState(initial = ThemeColors.Yellow.toArgb())
    val primaryColor = ThemeColors.fromArgb(savedColorInt)

    // Create dynamic color scheme based on user selection
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            secondary = primaryColor.copy(alpha = 0.7f),
            tertiary = primaryColor.copy(alpha = 0.5f),
            // Customize other colors as needed
            onPrimary = Color.White,
            onSecondary = Color.White,
            onTertiary = Color.White
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            secondary = primaryColor.copy(alpha = 0.7f),
            tertiary = primaryColor.copy(alpha = 0.5f),
            // Customize other colors as needed
            onPrimary = Color.White,
            onSecondary = Color.White,
            onTertiary = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Make sure you have Typography defined
        content = content
    )
}