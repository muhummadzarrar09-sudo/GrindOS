package com.grindos.app.ui.study

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grindos.app.domain.model.SprintMode
import com.grindos.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SprintTimerScreen(
    onNavigateBack: () -> Unit,
    viewModel: SprintTimerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sprint Timer ⏱️", color = TextPrimary) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Mode selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SprintMode.entries.forEach { mode ->
                    val selected = uiState.selectedMode == mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) ElectricBlue.copy(alpha = 0.2f) else DarkCard)
                            .clickable(enabled = !uiState.isRunning) { viewModel.selectMode(mode) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${mode.durationMinutes}m",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) ElectricBlue else TextSecondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Mode label
            Text(
                text = uiState.selectedMode.label,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            Spacer(Modifier.height(16.dp))

            // Timer display
            val progress = if (uiState.totalDurationSeconds > 0) {
                1f - (uiState.timeRemainingSeconds.toFloat() / uiState.totalDurationSeconds)
            } else 0f

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(220.dp),
                    color = ElectricBlue,
                    trackColor = DarkElevated,
                    strokeWidth = 8.dp,
                )
                Text(
                    text = viewModel.getFormattedTime(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(Modifier.height(32.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Reset
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(DarkCard)
                        .clickable { viewModel.resetTimer() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Refresh, "Reset", tint = TextSecondary)
                }

                // Play/Pause
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(ElectricBlue)
                        .clickable {
                            when {
                                !uiState.isRunning -> viewModel.startTimer()
                                uiState.isPaused -> viewModel.resumeTimer()
                                else -> viewModel.pauseTimer()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            !uiState.isRunning || uiState.isPaused -> Icons.Default.PlayArrow
                            else -> Icons.Default.Pause
                        },
                        contentDescription = if (uiState.isRunning && !uiState.isPaused) "Pause" else "Start",
                        tint = TextPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Stop
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(DarkCard)
                        .clickable { viewModel.resetTimer() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Stop, "Stop", tint = DangerRed)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Completed sprints
            Text(
                text = "${uiState.completedSprints} sprints completed today",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}
