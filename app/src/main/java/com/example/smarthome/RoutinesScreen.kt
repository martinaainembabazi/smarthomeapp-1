package com.example.smarthome

//import androidx.compose.material3.TimeInput
//import androidx.compose.material3.TimePickerState
//import androidx.compose.ui.platform.LocalContext

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.data.model.RecurrenceType
import com.example.smarthome.data.model.RoutineTask
import com.example.smarthome.data.model.toDisplayString
import com.example.smarthome.ui.viewModel.RoutineViewModel
import com.example.smarthome.ui.viewModel.RoutineViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.smarthome.ThemeViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen() {
    val application = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application
    val routineDao = com.example.smarthome.data.database.AppDatabase.getDatabase(application, kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.SupervisorJob())).routineDao()
    val repository = com.example.smarthome.data.repository.RoutineRepository(routineDao, application)
    val factory = RoutineViewModelFactory(application, repository)
    val viewModel: RoutineViewModel = viewModel(factory = factory)
    val routines by viewModel.allRoutines.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<RoutineTask?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Smart Home",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
//                    Color(0xFFFFD700)
                ),
                actions = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingTask = null
                    showAddDialog = true
                },
                shape = CircleShape,
                containerColor = Color(0xFF03A9F4)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Routine",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (routines.isEmpty()) {
                // Empty state
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_routine_empty),
                        contentDescription = "No Routines",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No Routines!",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Click the '+' button below to get started",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                // List of routines
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(routines) { routine ->
                        RoutineItem(
                            routine = routine,
                            onEdit = {
                                editingTask = routine
                                showAddDialog = true
                            },
                            onDelete = { viewModel.deleteRoutine(routine) }
                        )
                        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }

    // Show Add/Edit Dialog
    if (showAddDialog) {
        AddRoutineDialog(
            task = editingTask,
            onDismiss = { showAddDialog = false },
            onSave = { name, hour, minute, recurrence ->
                if (editingTask != null) {
                    viewModel.updateRoutine(
                        editingTask!!.copy(
                            name = name,
                            hour = hour,
                            minute = minute,
                            recurrence = recurrence
                        )
                    )
                } else {
                    viewModel.insertRoutine(
                        name = name,
                        hour = hour,
                        minute = minute,
                        recurrence = recurrence
                    )
                }
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoutineDialog(
    task: RoutineTask?,
    onDismiss: () -> Unit,
    onSave: (name: String, hour: Int, minute: Int, recurrence: RecurrenceType) -> Unit
) {
    // Initialize state variables with existing task values or defaults
    var taskName by remember { mutableStateOf(task?.name ?: "") }
    var selectedRecurrence by remember { mutableStateOf(task?.recurrence ?: RecurrenceType.EVERY_DAY) }

    // Initialize time picker state within the composable
    val timePickerState = rememberTimePickerState(
        initialHour = task?.hour ?: 8,
        initialMinute = task?.minute ?: 0
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = if (task != null) "Edit Routine" else "Add Routine",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Select Time:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TimePicker(
                    state = timePickerState
                )

                Text(
                    text = "Recurrence:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                RecurrenceDropdown(
                    selectedRecurrence = selectedRecurrence,
                    onRecurrenceSelected = { selectedRecurrence = it }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (taskName.isNotEmpty()) {
                                onSave(
                                    taskName,
                                    timePickerState.hour,
                                    timePickerState.minute,
                                    selectedRecurrence
                                )
                            }
                        }
                    ) {
                        Text(if (task != null) "Update" else "Add")
                    }
                }
            }
        }
    }
}

@Composable
fun RoutineItem(
    routine: RoutineTask,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Task Name: ${routine.name}",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Timing: ${formatTime(routine.hour, routine.minute)}",
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Recurrence: ${routine.recurrence.toDisplayString()}",
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        IconButton(onClick = onEdit) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF03A9F4)
            )
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFFE91E63)
            )
        }
    }
}

@Composable
fun RecurrenceDropdown(
    selectedRecurrence: RecurrenceType,
    onRecurrenceSelected: (RecurrenceType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedRecurrence.toDisplayString())
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            RecurrenceType.entries.forEach { recurrence ->
                DropdownMenuItem(
                    text = { Text(recurrence.toDisplayString()) },
                    onClick = {
                        onRecurrenceSelected(recurrence)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun formatTime(hour: Int, minute: Int): String {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val calendar = java.util.Calendar.getInstance()
    calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
    calendar.set(java.util.Calendar.MINUTE, minute)
    return timeFormat.format(calendar.time)
}