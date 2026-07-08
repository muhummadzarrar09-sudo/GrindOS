package com.grindos.app.ui.deen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grindos.app.ui.components.GrindCard
import com.grindos.app.ui.theme.*

@Composable
fun DeenScreen(
    onNavigateToPrayerTracker: () -> Unit,
    onNavigateToQuranTracker: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Deen 🤲",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        item {
            GrindCard(
                modifier = Modifier,
                containerColor = DeenEmerald.copy(alpha = 0.1f)
            ) {
                Icon(
                    Icons.Default.Mosque,
                    contentDescription = null,
                    tint = DeenEmerald,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Prayer Tracker",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Track your 5 daily prayers. Salah first, everything else gets barakah.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onNavigateToPrayerTracker,
                    colors = ButtonDefaults.buttonColors(containerColor = DeenEmerald),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Open Prayer Tracker")
                }
            }
        }

        item {
            GrindCard(
                modifier = Modifier,
                containerColor = DeenEmeraldDark.copy(alpha = 0.1f)
            ) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = DeenEmerald,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Quran Tracker",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Daily pages target, streak tracking, and reflection notes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onNavigateToQuranTracker,
                    colors = ButtonDefaults.buttonColors(containerColor = DeenEmeraldDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Open Quran Tracker")
                }
            }
        }

        item {
            GrindCard {
                Text(
                    text = "💡 Reminder",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeenEmerald
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "No guilt spiral if you miss a prayer. Just recover and keep going. Allah loves consistency, even if small.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
