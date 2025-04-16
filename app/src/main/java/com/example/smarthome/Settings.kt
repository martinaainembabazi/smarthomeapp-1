package com.example.smarthome.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Define DataStore at module level
val Context.dataStore by preferencesDataStore(name = "settings")

// Define preference keys
object PreferencesKeys {
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_EMAIL = stringPreferencesKey("user_email")
    val APP_COLOR = intPreferencesKey("app_color")
    val AUTO_ARM_SECURITY = booleanPreferencesKey("auto_arm_security")
    val APP_NOTIFICATIONS = booleanPreferencesKey("app_notifications")
}

// SettingsViewModel for managing settings data
class SettingsViewModel(private val context: Context) : ViewModel() {

    // User Settings
    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME] ?: "John Doe"
    }

    val userEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_EMAIL] ?: "john@someorg.com"
    }

    // App Settings
    val appColor: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.APP_COLOR] ?: Color(0xFFFFD500).toArgb() // Default yellow
    }

    val autoArmSecurity: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AUTO_ARM_SECURITY] ?: true
    }

    val appNotifications: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.APP_NOTIFICATIONS] ?: false
    }

    // Save settings functions
    fun saveUserName(name: String) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.USER_NAME] = name
            }
        }
    }

    fun saveUserEmail(email: String) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.USER_EMAIL] = email
            }
        }
    }

    fun saveAppColor(color: Int) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.APP_COLOR] = color
            }
        }
    }

    fun toggleAutoArmSecurity(enabled: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.AUTO_ARM_SECURITY] = enabled
            }
        }
    }

    fun toggleAppNotifications(enabled: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.APP_NOTIFICATIONS] = enabled
            }
        }
    }
}

// Factory for SettingsViewModel
class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Available color options for the app
val colorOptions = listOf(
    Color(0xFFFFD500), // Yellow (default)
    Color(0xFF2196F3), // Blue
    Color(0xFF4CAF50), // Green
    Color(0xFFF44336), // Red
    Color(0xFF9C27B0), // Purple
    Color(0xFFFF9800), // Orange
    Color(0xFF795548), // Brown
    Color(0xFF607D8B)  // Gray
)

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context)
    )

    // Collect data from DataStore
    val userName by viewModel.userName.collectAsState(initial = "John Doe")
    val userEmail by viewModel.userEmail.collectAsState(initial = "john@someorg.com")

    val autoArmSecurity by viewModel.autoArmSecurity.collectAsState(initial = true)
    val appNotifications by viewModel.appNotifications.collectAsState(initial = false)
    val appColorInt by viewModel.appColor.collectAsState(initial = Color(0xFFFFD500).toArgb())
    val currentAppColor = remember(appColorInt) { Color(appColorInt) }

    // State for dialogs
    var showUserEditDialog by remember { mutableStateOf(false) }
    var showColorPickerDialog by remember { mutableStateOf(false) }

    // Update the theme color when it changes. i have made a change here
//    LaunchedEffect(currentAppColor) {
//        onColorChanged(currentAppColor)
//    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // App Bar with dynamic color
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            color = currentAppColor
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "My Smart Home",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Settings Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f)
        ) {
            // User Settings Section
            SectionHeader(title = "User Settings")

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { showUserEditDialog = true },
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // User Icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // User Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = userEmail,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    // Edit Icon
                    IconButton(onClick = { showUserEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit User"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Settings Section
            SectionHeader(title = "App Settings")

            // App Color Setting
            SettingItem(
                title = "App Color:",
                trailing = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(currentAppColor)
                            .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                            .clickable { showColorPickerDialog = true }
                    )
                }
            )

            // Auto Arm Security Alarm
            SettingItem(
                title = "Auto Arm Security Alarm",
                trailing = {
                    Switch(
                        checked = autoArmSecurity,
                        onCheckedChange = { viewModel.toggleAutoArmSecurity(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = currentAppColor,
                            checkedTrackColor = currentAppColor.copy(alpha = 0.5f)
                        )
                    )
                }
            )

            // App Notifications
            SettingItem(
                title = "App Notifications",
                trailing = {
                    Switch(
                        checked = appNotifications,
                        onCheckedChange = { viewModel.toggleAppNotifications(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = currentAppColor,
                            checkedTrackColor = currentAppColor.copy(alpha = 0.5f)
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Voice Section (Visual only)
            SectionHeader(title = "Voice")
            SettingItem(
                title = "Voice Assistants",
                leading = {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = currentAppColor
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App Permissions Section (Visual only)
            SectionHeader(title = "App Permissions")
            SettingItem(
                title = "Notifications & Permissions",
                leading = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            )
        }
    }

    // User Edit Dialog
    if (showUserEditDialog) {
        var nameInput by remember { mutableStateOf(userName) }
        var emailInput by remember { mutableStateOf(userEmail) }

        Dialog(onDismissRequest = { showUserEditDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Edit User Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showUserEditDialog = false }
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                viewModel.saveUserName(nameInput)
                                viewModel.saveUserEmail(emailInput)
                                showUserEditDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = currentAppColor
                            )
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    // Color Picker Dialog
    if (showColorPickerDialog) {
        Dialog(onDismissRequest = { showColorPickerDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Choose App Color",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(colorOptions.size) { index ->
                            val color = colorOptions[index]
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(color)
                                    .border(
                                        width = if (color.toArgb() == appColorInt) 3.dp else 1.dp,
                                        color = if (color.toArgb() == appColorInt)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            Color.Gray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        viewModel.saveAppColor(color.toArgb())
                                        showColorPickerDialog = false
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showColorPickerDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun SectionHeader(title: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = Color.LightGray.copy(alpha = 0.3f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun SettingItem(
    title: String,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leading != null) {
            leading()
            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp
        )

        if (trailing != null) {
            trailing()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}