package com.grindos.app.ui.life

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
import com.grindos.app.domain.model.HackathonTask
import com.grindos.app.domain.model.KanbanStatus
import com.grindos.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HackathonScreen(
    onNavigateBack: () -> Unit,
    viewModel: LifeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var newTaskTitle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hackathon Board 💻", color = TextPrimary) },
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        placeholder = { Text("New task...", color = TextMuted) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HypeOrange,
                            unfocusedBorderColor = DarkElevated,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = {
                        if (newTaskTitle.isNotBlank()) {
                            viewModel.addHackathonTask(newTaskTitle.trim())
                            newTaskTitle = ""
                        }
                    }) {
                        Icon(Icons.Default.Add, "Add", tint = HypeOrange)
                    }
                }
            }

            // Kanban columns
            KanbanStatus.entries.forEach { status ->
                val tasks = uiState.hackathonTasks.filter { it.status == status }
                item {
                    Text(
                        text = "${status.name} (${tasks.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = getKanbanColor(status)
                    )
                }
                items(tasks) { task ->
                    HackathonTaskItem(
                        task = task,
                        onMoveForward = {
                            val next = when (status) {
                                KanbanStatus.BACKLOG -> KanbanStatus.DOING
                                KanbanStatus.DOING -> KanbanStatus.DONE
                                KanbanStatus.DONE -> KanbanStatus.BACKLOG
                            }
                            viewModel.updateHackathonStatus(task.id, next)
                        },
                        onDelete = { viewModel.deleteHackathonTask(task) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun HackathonTaskItem(
    task: HackathonTask,
    onMoveForward: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(task.title, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
            task.deadline?.let {
                Text("Due: $it", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
        }
        IconButton(onClick = onMoveForward) {
            Icon(Icons.Default.ArrowForward, "Move", tint = HypeOrange)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, "Delete", tint = DangerRed)
        }
    }
}

private fun getKanbanColor(status: KanbanStatus) = when (status) {
    KanbanStatus.BACKLOG -> TextSecondary
    KanbanStatus.DOING -> HypeOrange
    KanbanStatus.DONE -> NeonGreen
}
