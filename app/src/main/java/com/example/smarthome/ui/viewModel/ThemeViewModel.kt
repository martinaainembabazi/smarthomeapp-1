//package com.example.smarthome.ui.viewModel
//import androidx.lifecycle.viewModelScope
//
//import android.app.Application
//import androidx.compose.ui.graphics.toArgb
//import androidx.datastore.preferences.core.edit
//import androidx.lifecycle.AndroidViewModel
//import com.example.smarthome.ui.screens.PreferencesKeys
//import com.example.smarthome.ui.screens.dataStore
//
//import com.example.smarthome.ui.theme.ThemeColor
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.launch
//
//class ThemeViewModel(application: Application) : AndroidViewModel(application) {
//    private val _currentThemeColor = MutableStateFlow(ThemeColor.Yellow)
//    val currentThemeColor: StateFlow<ThemeColor> = _currentThemeColor
//
//    init {
//        loadSavedTheme()
//    }
//
//    private fun loadSavedTheme() {
//        viewModelScope.launch {
//            val context = getApplication<Application>()
//            val savedColorInt = context.dataStore.data.map { preferences ->
//                preferences[PreferencesKeys.APP_COLOR] ?: ThemeColor.Yellow.color.toArgb()
//            }.collect { colorInt ->
//                _currentThemeColor.value = ThemeColor.fromArgb(colorInt) as ThemeColor.Yellow
//            }
//        }
//    }
//
//    fun updateThemeColor(newColor: ThemeColor) {
//        viewModelScope.launch {
//            val context = getApplication<Application>()
//            context.dataStore.edit { preferences ->
//                preferences[PreferencesKeys.APP_COLOR] = newColor.color.toArgb()
//            }
//            _currentThemeColor.value = newColor as ThemeColor.Yellow
//        }
//    }
//}