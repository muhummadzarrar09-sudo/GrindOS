package com.grindos.app.ui.deen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grindos.app.ui.components.GrindCard
import com.grindos.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranTrackerScreen(
    onNavigateBack: () -> Unit,
    viewModel: DeenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var pagesRead by remember { mutableIntStateOf(uiState.quranEntry?.pagesRead ?: 0) }

    LaunchedEffect(uiState.quranEntry?.pagesRead) {
        pagesRead = uiState.quranEntry?.pagesRead ?: 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quran Tracker 📖", color = TextPrimary) },
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GrindCard {
                    Text(
                        text = "Today's Quran",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pages Read",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                        Text(
                            text = "$pagesRead / ${uiState.dailyQuranTarget}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeenEmerald
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Slider(
                        value = pagesRead.toFloat(),
                        onValueChange = { pagesRead = it.toInt() },
                        valueRange = 0f..20f,
                        steps = 19,
                        colors = SliderDefaults.colors(
                            thumbColor = DeenEmerald,
                            activeTrackColor = DeenEmerald,
                            inactiveTrackColor = DarkElevated
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.updateQuranPages(pagesRead) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = DeenEmerald)
                    ) {
                        Text("Save Progress")
                    }

                    if (pagesRead >= uiState.dailyQuranTarget && uiState.dailyQuranTarget > 0) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "✅ Target met! Big barakah today 🤲",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NeonGreen
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
