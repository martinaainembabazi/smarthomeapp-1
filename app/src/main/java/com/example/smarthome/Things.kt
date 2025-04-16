package com.example.smarthome.ui.screens

//import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthome.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingsScreen( ) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Smart Home",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =MaterialTheme.colorScheme.primary
//                    Color(0xFFFFD700)
                ),
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                windowInsets = WindowInsets.statusBars
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                // Custom grid of circles and plus signs to match the image
                ThingsGrid()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No things!",
                    fontSize = 18.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "It looks like we didn't discover any devices.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Text(
                    text = "Try an option below.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons
                ActionButton(
                    icon = Icons.Default.Search,
                    text = "Run discovery",
                    color =MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                ActionButton(
                    icon = Icons.Default.Add,
                    text = "Add a cloud account",
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    ActionButton(
//                        icon = Icons.Default.Search,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Text("Run discovery")
//                }
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                )}

                ActionButton(
                    icon = Icons.Default.Apps,
                    text = "View our supported devices",
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                ActionButton(
                    icon = Icons.Default.Email,
                    text = "Contact support",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ThingsGrid() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CircleIcon()
            CircleIcon()
            CircleIcon()
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CircleIcon()
            CircleIcon()
            PlusIcon()
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CircleIcon()
            PlusIcon()
            PlusIcon()
        }
    }
}

@Composable
fun CircleIcon() {
    Box(
        modifier = Modifier
            .size(20.dp)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_circle),
            contentDescription = "Circle",
            tint = Color.Gray
        )
    }
}

@Composable
fun PlusIcon() {
    Box(
        modifier = Modifier
            .size(20.dp)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Plus",
            tint = Color.Gray
        )
    }
}

@Composable
fun ActionButton(icon: ImageVector, text: String, color: Color) {
    Button(
        onClick = { /* Do nothing as per requirement */ },
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = CircleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )

            Text(
                text = text,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}