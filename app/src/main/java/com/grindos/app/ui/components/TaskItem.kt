package com.grindos.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.grindos.app.domain.model.Task
import com.grindos.app.domain.model.TaskCategory
import com.grindos.app.domain.model.TaskPriority
import com.grindos.app.ui.theme.*
import com.grindos.app.util.DateTimeUtils

@Composable
fun TaskItem(
    task: Task,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .clickable(onClick = onToggleComplete)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Priority indicator
        Box(
            modifier = Modifier
                .size(4.dp, 32.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(getPriorityColor(task.priority))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Checkbox
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onToggleComplete() },
            colors = CheckboxDefaults.colors(
                checkedColor = NeonGreen,
                uncheckedColor = TextMuted
            )
        )

        // Task info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (task.isCompleted) TextMuted else TextPrimary,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = task.category.name.replace("_", " "),
                    style = MaterialTheme.typography.labelSmall,
                    color = getCategoryColor(task.category)
                )
                task.time?.let {
                    Text(
                        text = DateTimeUtils.formatTime(it),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                task.durationMinutes?.let {
                    Text(
                        text = "${it}min",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // Delete button
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete task",
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun getPriorityColor(priority: TaskPriority): Color = when (priority) {
    TaskPriority.HIGH -> PriorityHigh
    TaskPriority.MEDIUM -> PriorityMedium
    TaskPriority.LOW -> PriorityLow
}

private fun getCategoryColor(category: TaskCategory): Color = when (category) {
    TaskCategory.STUDY -> ElectricBlue
    TaskCategory.PRAYER -> DeenEmerald
    TaskCategory.QURAN -> DeenEmerald
    TaskCategory.HACKATHON -> HypeOrange
    TaskCategory.BOOK -> LevelPurple
    TaskCategory.GOLF -> NeonGreen
    TaskCategory.PERSONAL -> TextSecondary
}
