package com.grindos.app.util

import com.grindos.app.domain.model.SprintMode

object XpCalculator {

    // XP Rewards
    const val XP_TASK_COMPLETE = 10
    const val XP_TASK_HIGH_PRIORITY = 15
    const val XP_PRAYER_ON_TIME = 15
    const val XP_QURAN_PAGE = 5
    const val XP_QURAN_TARGET_MET = 10
    const val XP_SPRINT_MINI = 5
    const val XP_SPRINT_FOCUS = 15
    const val XP_SPRINT_DEEP = 30
    const val XP_SPRINT_MCQ = 10
    const val XP_ERROR_LOG_FIXED = 10
    const val XP_HACKATHON_TASK = 20
    const val XP_BOOK_TARGET = 10
    const val XP_GOLF_SESSION = 10

    // Level thresholds
    private val levels = listOf(
        0 to "Rookie",
        100 to "Getting Started",
        300 to "Locked In",
        600 to "Demon Mode",
        1000 to "Scholarship Hunter",
        2000 to "FAST Warrior",
        3500 to "Unstoppable",
        5000 to "Legend",
        8000 to "GrindOS Master"
    )

    fun calculateSprintXp(mode: SprintMode): Int = when (mode) {
        SprintMode.MINI_RESET -> XP_SPRINT_MINI
        SprintMode.FOCUS_SPRINT -> XP_SPRINT_FOCUS
        SprintMode.DEEP_WORK -> XP_SPRINT_DEEP
        SprintMode.MCQ_SPEED_DRILL -> XP_SPRINT_MCQ
    }

    fun getLevelForXp(totalXp: Int): String {
        var currentLevel = "Rookie"
        for ((threshold, name) in levels) {
            if (totalXp >= threshold) currentLevel = name
            else break
        }
        return currentLevel
    }

    fun getNextLevelThreshold(totalXp: Int): Int {
        for ((threshold, _) in levels) {
            if (totalXp < threshold) return threshold
        }
        return levels.last().first + 1000
    }

    fun getLevelProgress(totalXp: Int): Float {
        val currentThreshold = levels.lastOrNull { totalXp >= it.first }?.first ?: 0
        val nextThreshold = getNextLevelThreshold(totalXp)
        val range = nextThreshold - currentThreshold
        if (range <= 0) return 1f
        return ((totalXp - currentThreshold).toFloat() / range).coerceIn(0f, 1f)
    }

    fun calculateStreakBonus(streak: Int): Int = when {
        streak >= 30 -> 50
        streak >= 14 -> 25
        streak >= 7 -> 15
        streak >= 3 -> 5
        else -> 0
    }
}
