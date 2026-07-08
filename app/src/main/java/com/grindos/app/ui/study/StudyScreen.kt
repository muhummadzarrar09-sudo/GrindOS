package com.grindos.app.ui.study

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grindos.app.domain.model.StudySection
import com.grindos.app.domain.model.StudyTopic
import com.grindos.app.domain.model.TopicStatus
import com.grindos.app.ui.components.GrindCard
import com.grindos.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    onNavigateToSprintTimer: () -> Unit,
    onNavigateToErrorLog: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Text(
                text = "FAST Prep 📚",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Progress overview
        item {
            GrindCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "FAST Roadmap",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "${uiState.masteredCount}/${uiState.totalCount} topics mastered",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                    if (uiState.totalCount > 0) {
                        CircularProgressIndicator(
                            progress = { uiState.masteredCount.toFloat() / uiState.totalCount },
                            modifier = Modifier.size(48.dp),
                            color = ElectricBlue,
                            trackColor = DarkElevated,
                        )
                    }
                }
            }
        }

        // Section filter chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedSection == null,
                        onClick = { viewModel.selectSection(null) },
                        label = { Text("All") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricBlue.copy(alpha = 0.2f),
                            selectedLabelColor = ElectricBlue
                        )
                    )
                }
                items(StudySection.entries.toList()) { section ->
                    FilterChip(
                        selected = uiState.selectedSection == section,
                        onClick = { viewModel.selectSection(section) },
                        label = { Text(section.name.replace("_", " ")) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricBlue.copy(alpha = 0.2f),
                            selectedLabelColor = ElectricBlue
                        )
                    )
                }
            }
        }

        // Topics list
        items(uiState.topics) { topic ->
            StudyTopicItem(
                topic = topic,
                onStatusChange = { status -> viewModel.updateTopicStatus(topic, status) }
            )
        }

        // Action buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateToSprintTimer,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Icon(Icons.Default.Timer, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Sprint Timer")
                }
                Button(
                    onClick = onNavigateToErrorLog,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.8f))
                ) {
                    Icon(Icons.Default.BugReport, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Error Log")
                }
            }
        }

        // Empty state
        if (uiState.topics.isEmpty() && !uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "No topics yet. Add FAST topics to start tracking!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun StudyTopicItem(
    topic: StudyTopic,
    onStatusChange: (TopicStatus) -> Unit
) {
    val statusColor = when (topic.status) {
        TopicStatus.NOT_STARTED -> TextMuted
        TopicStatus.LEARNING -> InfoBlue
        TopicStatus.PRACTICING -> WarningOrange
        TopicStatus.TIMED -> HypeOrange
        TopicStatus.MASTERED -> NeonGreen
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .clickable {
                val nextStatus = when (topic.status) {
                    TopicStatus.NOT_STARTED -> TopicStatus.LEARNING
                    TopicStatus.LEARNING -> TopicStatus.PRACTICING
                    TopicStatus.PRACTICING -> TopicStatus.TIMED
                    TopicStatus.TIMED -> TopicStatus.MASTERED
                    TopicStatus.MASTERED -> TopicStatus.NOT_STARTED
                }
                onStatusChange(nextStatus)
            }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(4.dp, 36.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(statusColor)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = topic.name,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
            Text(
                text = topic.section.name.replace("_", " "),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(statusColor.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = topic.status.name.replace("_", " "),
                style = MaterialTheme.typography.labelSmall,
                color = statusColor
            )
        }
    }
}
