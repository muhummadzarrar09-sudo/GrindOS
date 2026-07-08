package com.grindos.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grindos.app.ui.components.*
import com.grindos.app.ui.theme.*
import com.grindos.app.util.DateTimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSprintTimer: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Greeting header
        item {
            Column {
                Text(
                    text = DateTimeUtils.getGreeting() + " 💪",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Today's Mission — ${DateTimeUtils.formatDate(DateTimeUtils.getToday())}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // XP / Streak Card
        item {
            XpStreakCard(
                totalXp = uiState.totalXp,
                currentStreak = uiState.currentStreak,
                level = uiState.level,
                levelProgress = uiState.levelProgress
            )
        }

        // Top Tasks
        item {
            GrindCard(title = "🎯 Top Priorities") {
                if (uiState.topTasks.isEmpty()) {
                    Text(
                        text = "No tasks yet. Add one from Study tab!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.topTasks.forEach { task ->
                            TaskItem(
                                task = task,
                                onToggleComplete = { viewModel.toggleTaskComplete(task) },
                                onDelete = { viewModel.deleteTask(task) }
                            )
                        }
                    }
                }
            }
        }

        // Prayer Status
        item {
            GrindCard(title = "🕌 Prayers (${uiState.completedPrayers}/5)") {
                if (uiState.prayers.isNotEmpty()) {
                    PrayerStatusRow(
                        prayers = uiState.prayers,
                        onPrayerToggle = { prayerId -> viewModel.togglePrayer(prayerId) }
                    )
                } else {
                    Text(
                        text = "Loading prayers...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        // Quran Progress
        item {
            GrindCard(title = "📖 Quran") {
                val entry = uiState.quranEntry
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Today's Pages",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "${entry?.pagesRead ?: 0} / ${entry?.targetPages ?: 2}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeenEmerald
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = DeenEmerald,
                        modifier = Modifier.size(40.dp)
                    )
                }
                if (entry != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = {
                            if (entry.targetPages > 0) {
                                (entry.pagesRead.toFloat() / entry.targetPages).coerceIn(0f, 1f)
                            } else 0f
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = DeenEmerald,
                        trackColor = DarkElevated,
                    )
                }
            }
        }

        // Sprint Timer Card
        item {
            SprintTimerCard(
                currentSprint = null,
                isRunning = false,
                timeRemaining = "25:00",
                onStartSprint = onNavigateToSprintTimer
            )
        }

        // Panic Button
        item {
            PanicButton(onClick = { viewModel.triggerPanic() })
        }

        // Bottom spacer
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

    // Panic Dialog
    if (uiState.showPanicDialog) {
        PanicDialog(
            message = uiState.panicMessage,
            suggestedTask = uiState.panicSuggestion,
            onStartTimer = {
                viewModel.dismissPanic()
                onNavigateToSprintTimer()
            },
            onDismiss = { viewModel.dismissPanic() }
        )
    }
}
