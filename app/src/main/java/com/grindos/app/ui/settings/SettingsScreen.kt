package com.grindos.app.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grindos.app.domain.model.NotificationTone
import com.grindos.app.ui.components.GrindCard
import com.grindos.app.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showNotificationModeDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Settings ⚙️",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Notification Mode
        item {
            GrindCard(title = "🔔 Notification Mode") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showNotificationModeDialog = true }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Current Mode",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = uiState.settings.notificationMode.name.replace("_", " "),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ElectricBlue
                        )
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = TextMuted)
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = getNotificationModeDescription(uiState.settings.notificationMode),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Default Reminder
        item {
            GrindCard(title = "⏰ Default Reminder") {
                var reminderMinutes by remember { mutableIntStateOf(uiState.settings.defaultReminderMinutes) }
                Text(
                    text = "${reminderMinutes} minutes before",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                Spacer(Modifier.height(8.dp))
                Slider(
                    value = reminderMinutes.toFloat(),
                    onValueChange = { reminderMinutes = it.toInt() },
                    onValueChangeFinished = { viewModel.setDefaultReminderMinutes(reminderMinutes) },
                    valueRange = 5f..30f,
                    steps = 4,
                    colors = SliderDefaults.colors(
                        thumbColor = ElectricBlue,
                        activeTrackColor = ElectricBlue,
                        inactiveTrackColor = DarkElevated
                    )
                )
            }
        }

        // Daily Targets
        item {
            GrindCard(title = "🎯 Daily Targets") {
                var studyTarget by remember { mutableIntStateOf(uiState.settings.dailyStudyTargetMinutes) }
                var quranTarget by remember { mutableIntStateOf(uiState.settings.dailyQuranPages) }
                var bookTarget by remember { mutableIntStateOf(uiState.settings.dailyBookPages) }

                // Study target
                TargetSlider(
                    label = "Study Target",
                    value = studyTarget,
                    unit = "min",
                    range = 30f..300f,
                    steps = 26,
                    color = ElectricBlue,
                    onValueChange = { studyTarget = it },
                    onFinished = { viewModel.setDailyStudyTarget(studyTarget) }
                )
                Spacer(Modifier.height(12.dp))

                // Quran target
                TargetSlider(
                    label = "Quran Pages",
                    value = quranTarget,
                    unit = "pages",
                    range = 1f..20f,
                    steps = 18,
                    color = DeenEmerald,
                    onValueChange = { quranTarget = it },
                    onFinished = { viewModel.setDailyQuranPages(quranTarget) }
                )
                Spacer(Modifier.height(12.dp))

                // Book target
                TargetSlider(
                    label = "Book Pages",
                    value = bookTarget,
                    unit = "pages",
                    range = 5f..50f,
                    steps = 8,
                    color = LevelPurple,
                    onValueChange = { bookTarget = it },
                    onFinished = { viewModel.setDailyBookPages(bookTarget) }
                )
            }
        }

        // App Info
        item {
            GrindCard {
                Text("GrindOS v1.0.0", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Personal command center for FAST 2027 prep, prayers, Quran, books, golf, hackathon tasks, and ADHD-friendly daily planning.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "\"No shame spiral. Pick one task. Restart now.\" 🔥",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HypeOrange,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

    // Notification mode dialog
    if (showNotificationModeDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationModeDialog = false },
            containerColor = DarkSurface,
            title = { Text("Notification Mode", color = TextPrimary) },
            text = {
                Column {
                    NotificationTone.entries.forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setNotificationMode(mode)
                                    showNotificationModeDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = uiState.settings.notificationMode == mode,
                                onClick = {
                                    viewModel.setNotificationMode(mode)
                                    showNotificationModeDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = ElectricBlue)
                            )
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(mode.name.replace("_", " "), color = TextPrimary)
                                Text(
                                    getNotificationModeDescription(mode),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationModeDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun TargetSlider(
    label: String,
    value: Int,
    unit: String,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
    color: androidx.compose.ui.graphics.Color,
    onValueChange: (Int) -> Unit,
    onFinished: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Text("$value $unit", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
    }
    Slider(
        value = value.toFloat(),
        onValueChange = { onValueChange(it.toInt()) },
        onValueChangeFinished = onFinished,
        valueRange = range,
        steps = steps,
        colors = SliderDefaults.colors(
            thumbColor = color,
            activeTrackColor = color,
            inactiveTrackColor = DarkElevated
        )
    )
}

private fun getNotificationModeDescription(mode: NotificationTone): String = when (mode) {
    NotificationTone.GENTLE -> "Calm reminders. Small wins."
    NotificationTone.HYPE -> "BRO LOCK IN 🔥 High energy."
    NotificationTone.ROAST -> "Funny aggressive accountability 💀"
    NotificationTone.DEEN_FIRST -> "Prayer/Quran-centered tone 🤲"
    NotificationTone.EXAM_WAR -> "FAST-focused intense mode 🎯"
}
