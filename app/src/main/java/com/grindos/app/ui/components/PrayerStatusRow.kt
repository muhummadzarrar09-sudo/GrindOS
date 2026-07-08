package com.grindos.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grindos.app.domain.model.Prayer
import com.grindos.app.ui.theme.*
import com.grindos.app.util.DateTimeUtils

@Composable
fun PrayerStatusRow(
    prayers: List<Prayer>,
    onPrayerToggle: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        prayers.forEach { prayer ->
            PrayerChip(
                prayer = prayer,
                onToggle = { onPrayerToggle(prayer.id) }
            )
        }
    }
}

@Composable
private fun PrayerChip(
    prayer: Prayer,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (prayer.completed) DeenEmerald.copy(alpha = 0.15f) else DarkElevated)
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status icon
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (prayer.completed) DeenEmerald else DarkCard),
            contentAlignment = Alignment.Center
        ) {
            if (prayer.completed) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Prayer name
        Text(
            text = prayer.prayerName.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyLarge,
            color = if (prayer.completed) DeenEmerald else TextPrimary,
            modifier = Modifier.weight(1f)
        )

        // Time
        Text(
            text = DateTimeUtils.formatTime(prayer.time),
            style = MaterialTheme.typography.bodyMedium,
            color = if (prayer.completed) DeenEmerald.copy(alpha = 0.7f) else TextSecondary
        )
    }
}
