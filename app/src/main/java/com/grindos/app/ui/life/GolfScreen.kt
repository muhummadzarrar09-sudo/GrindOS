package com.grindos.app.ui.life

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grindos.app.ui.components.GrindCard
import com.grindos.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GolfScreen(
    onNavigateBack: () -> Unit,
    viewModel: LifeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var drillName by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("30") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Golf 🏌️", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                GrindCard {
                    Text("Log Practice", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = drillName,
                        onValueChange = { drillName = it },
                        label = { Text("Drill Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = DarkElevated,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it.filter { c -> c.isDigit() } },
                        label = { Text("Duration (minutes)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = DarkElevated,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (drillName.isNotBlank()) {
                                viewModel.addGolfEntry(drillName.trim(), duration.toIntOrNull() ?: 30)
                                drillName = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                    ) { Text("Log Session") }
                }
            }

            item {
                Text("Practice History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            items(uiState.golfEntries) { entry ->
                GrindCard {
                    Text(entry.drillName, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text("${entry.durationMinutes} min on ${entry.date}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    entry.notes?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = TextMuted) }
                }
            }

            if (uiState.golfEntries.isEmpty()) {
                item {
                    Text("No practice logged yet. Touch grass professionally! 🏌️", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
