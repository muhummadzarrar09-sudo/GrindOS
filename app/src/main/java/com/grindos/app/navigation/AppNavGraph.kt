package com.grindos.app.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grindos.app.ui.deen.DeenScreen
import com.grindos.app.ui.deen.PrayerTrackerScreen
import com.grindos.app.ui.deen.QuranTrackerScreen
import com.grindos.app.ui.home.HomeScreen
import com.grindos.app.ui.life.*
import com.grindos.app.ui.settings.SettingsScreen
import com.grindos.app.ui.study.ErrorLogScreen
import com.grindos.app.ui.study.SprintTimerScreen
import com.grindos.app.ui.study.StudyScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in Screen.bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) + fadeOut() },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) + fadeOut() }
        ) {
            // Bottom nav destinations
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToSprintTimer = { navController.navigate(Screen.SprintTimer.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }
            composable(Screen.Study.route) {
                StudyScreen(
                    onNavigateToSprintTimer = { navController.navigate(Screen.SprintTimer.route) },
                    onNavigateToErrorLog = { navController.navigate(Screen.ErrorLog.route) }
                )
            }
            composable(Screen.Deen.route) {
                DeenScreen(
                    onNavigateToPrayerTracker = { navController.navigate(Screen.PrayerTracker.route) },
                    onNavigateToQuranTracker = { navController.navigate(Screen.QuranTracker.route) }
                )
            }
            composable(Screen.Life.route) {
                LifeScreen(
                    onNavigateToHackathon = { navController.navigate(Screen.Hackathon.route) },
                    onNavigateToBooks = { navController.navigate(Screen.Books.route) },
                    onNavigateToGolf = { navController.navigate(Screen.Golf.route) }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }

            // Sub-screens
            composable(Screen.SprintTimer.route) {
                SprintTimerScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.ErrorLog.route) {
                ErrorLogScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.PrayerTracker.route) {
                PrayerTrackerScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.QuranTracker.route) {
                QuranTrackerScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Hackathon.route) {
                HackathonScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Books.route) {
                BooksScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Golf.route) {
                GolfScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}

fun NavHostController.navigateToBottomNavScreen(screen: Screen) {
    navigate(screen.route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
