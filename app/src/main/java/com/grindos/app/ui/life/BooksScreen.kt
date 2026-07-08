package com.grindos.app.ui.life

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grindos.app.ui.components.GrindCard
import com.grindos.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(
    onNavigateBack: () -> Unit,
    viewModel: LifeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var bookName by remember { mutableStateOf("") }
    var pages by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Books 📚", color = TextPrimary) },
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
                GrindCard {
                    Text("Log Reading", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = bookName,
                        onValueChange = { bookName = it },
                        label = { Text("Book Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LevelPurple,
                            unfocusedBorderColor = DarkElevated,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pages,
                        onValueChange = { pages = it.filter { c -> c.isDigit() } },
                        label = { Text("Pages Read Today") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LevelPurple,
                            unfocusedBorderColor = DarkElevated,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (bookName.isNotBlank() && pages.isNotBlank()) {
                                viewModel.addBookEntry(bookName.trim(), pages.toIntOrNull() ?: 0)
                                bookName = ""
                                pages = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = LevelPurple)
                    ) { Text("Log Pages") }
                }
            }

            item {
                Text("Reading History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            items(uiState.bookEntries) { entry ->
                GrindCard {
                    Text(entry.bookName, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text("${entry.pagesReadToday} pages on ${entry.date}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }

            if (uiState.bookEntries.isEmpty()) {
                item {
                    Text("No reading logged yet. Start tracking! 📖", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
