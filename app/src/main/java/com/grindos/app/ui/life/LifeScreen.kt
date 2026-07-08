package com.grindos.app.ui.life

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grindos.app.ui.components.GrindCard
import com.grindos.app.ui.theme.*

@Composable
fun LifeScreen(
    onNavigateToHackathon: () -> Unit,
    onNavigateToBooks: () -> Unit,
    onNavigateToGolf: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Life 🌱",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        item {
            GrindCard(containerColor = HypeOrange.copy(alpha = 0.1f)) {
                Icon(Icons.Default.Code, null, tint = HypeOrange, modifier = Modifier.size(40.dp))
                Spacer(Modifier.height(12.dp))
                Text("Hackathon", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Kanban board, deadlines, build sprints.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onNavigateToHackathon,
                    colors = ButtonDefaults.buttonColors(containerColor = HypeOrange),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Open Board") }
            }
        }

        item {
            GrindCard(containerColor = LevelPurple.copy(alpha = 0.1f)) {
                Icon(Icons.Default.Book, null, tint = LevelPurple, modifier = Modifier.size(40.dp))
                Spacer(Modifier.height(12.dp))
                Text("Books", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Daily reading pages and notes.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onNavigateToBooks,
                    colors = ButtonDefaults.buttonColors(containerColor = LevelPurple),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Open Books") }
            }
        }

        item {
            GrindCard(containerColor = NeonGreen.copy(alpha = 0.1f)) {
                Icon(Icons.Default.SportsGolf, null, tint = NeonGreen, modifier = Modifier.size(40.dp))
                Spacer(Modifier.height(12.dp))
                Text("Golf", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Practice sessions and drills.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onNavigateToGolf,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Open Golf") }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
