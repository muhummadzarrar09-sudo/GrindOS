package com.grindos.app.ui.study

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grindos.app.domain.model.ErrorLog
import com.grindos.app.domain.model.ErrorStatus
import com.grindos.app.domain.model.MistakeType
import com.grindos.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorLogScreen(
    onNavigateBack: () -> Unit,
    viewModel: ErrorLogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Error Log 🐛", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.toggleAddDialog() },
                containerColor = DangerRed
            ) {
                Icon(Icons.Default.Add, "Add Error", tint = TextPrimary)
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.errorLogs) { errorLog ->
                ErrorLogItem(
                    errorLog = errorLog,
                    onMarkFixed = { viewModel.markFixed(errorLog) },
                    onDelete = { viewModel.deleteErrorLog(errorLog) }
                )
            }

            if (uiState.errorLogs.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No errors logged yet. Track mistakes to improve! 🎯",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorLogItem(
    errorLog: ErrorLog,
    onMarkFixed: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor = if (errorLog.status == ErrorStatus.FIXED) NeonGreen else WarningOrange

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = errorLog.topic,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = errorLog.status.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(text = errorLog.section, style = MaterialTheme.typography.labelSmall, color = ElectricBlue)
        Spacer(Modifier.height(4.dp))
        Text(text = errorLog.question, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Spacer(Modifier.height(4.dp))
        Text(text = "Type: ${errorLog.mistakeType.name}", style = MaterialTheme.typography.labelSmall, color = HypeOrange)
        Spacer(Modifier.height(4.dp))
        Text(text = "Why: ${errorLog.whyItHappened}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(text = "Fix: ${errorLog.correctMethod}", style = MaterialTheme.typography.bodySmall, color = NeonGreen)

        if (errorLog.status == ErrorStatus.OPEN) {
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onMarkFixed) {
                    Text("Mark Fixed ✓", color = NeonGreen)
                }
                TextButton(onClick = onDelete) {
                    Text("Delete", color = DangerRed)
                }
            }
        }
    }
}
