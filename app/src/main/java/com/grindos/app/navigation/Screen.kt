package com.grindos.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    // Bottom nav destinations
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Study : Screen("study", "Study", Icons.Default.School)
    data object Deen : Screen("deen", "Deen", Icons.Default.Mosque)
    data object Life : Screen("life", "Life", Icons.Default.SelfImprovement)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    // Sub-screens
    data object SprintTimer : Screen("sprint_timer", "Sprint Timer", Icons.Default.Timer)
    data object ErrorLog : Screen("error_log", "Error Log", Icons.Default.BugReport)
    data object PrayerTracker : Screen("prayer_tracker", "Prayer Tracker", Icons.Default.Mosque)
    data object QuranTracker : Screen("quran_tracker", "Quran Tracker", Icons.Default.MenuBook)
    data object Hackathon : Screen("hackathon", "Hackathon", Icons.Default.Code)
    data object Books : Screen("books", "Books", Icons.Default.Book)
    data object Golf : Screen("golf", "Golf", Icons.Default.SportsGolf)

    companion object {
        val bottomNavItems = listOf(Home, Study, Deen, Life, Settings)
    }
}
